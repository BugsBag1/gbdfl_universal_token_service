package kz.oib.gbdfl_universal_token_service.service;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import kz.bee.bip.syncchannel.v10.types.response.SyncSendMessageResponse;
import kz.oib.common.model.ServiceReturn;
import kz.oib.common.util.ShepConnectorRequestUtil;
import kz.oib.gbdfl_universal_token_service.config.AppConstants;
import kz.oib.gbdfl_universal_token_service.exception.ExternalServiceException;
import kz.oib.gbdfl_universal_token_service.model.dto.RequestDTO;
import kz.oib.gbdfl_universal_token_service.model.dto.ResponseDTO;
import kz.oib.gbdfl_universal_token_service.model.mapper.RequestMapper;
import kz.oib.gbdfl_universal_token_service.model.mapper.ResponseMapper;
import kz.oib.gbdfl_universal_token_service.xsd.RequestAndResponse.ObjectFactory;
import kz.oib.gbdfl_universal_token_service.xsd.RequestAndResponse.Request;
import kz.oib.gbdfl_universal_token_service.xsd.RequestAndResponse.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.UUID;

import static kz.oib.common.util.MarshallerUtil.convertServiceReturn;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppService {
    private final AppConstants appConstants;
    private final RequestMapper requestMapper;
    private final ResponseMapper responseMapper;
    private final RestTemplate restTemplate;
    private final Environment env;


    public ResponseDTO getPersonFlByIIN(String iin) {
        try {
            final Short channelType = 0;
            RequestDTO requestDTO = new RequestDTO();
            requestDTO.setMessageId(UUID.randomUUID().toString());
            requestDTO.setMessageDate(LocalDateTime.now());
            requestDTO.setSenderCode(appConstants.getSenderCode());
            requestDTO.setIin(iin);

            // TODO
            // Тут нужно КДП токен взять с КДП сервиса, пока статично тест поставлю
            requestDTO.setKdpToken("Test");
            requestDTO.setPublicKey("Test");

            Request request = requestMapper.map(requestDTO);
            String data = wrapRequestChildrenInData(request);

            HttpEntity<String> httpEntity = ShepConnectorRequestUtil.builder()
                    .xmlData(data)
                    .appName(env.getProperty("spring.application.name"))
                    .channelType(appConstants.getChanelType())
                    .needTransportSign(true)
                    .sendAsXml(true)
                    .wrapByTagData(false)
                    .serviceId(appConstants.getServiceId())
                    .messageId(null)
                    .build()
                    .buildHttpEntity();

            ServiceReturn serviceReturn = restTemplate.postForObject(appConstants.getVshepUrl(), httpEntity, ServiceReturn.class);

            Object returnMessage = serviceReturn.getReturnMessage();
            if (serviceReturn == null || serviceReturn.getReturnMessage() == null) {
                log.warn("Пустой ответ от ШЭП");
                throw new ExternalServiceException("Не удалось получить ответ от ШЭП");
            }

            SyncSendMessageResponse syncResp = convertServiceReturn(serviceReturn, SyncSendMessageResponse.class);
            Object responseData = syncResp.getResponseData().getData();
            String dataXml = (responseData != null) ? responseData.toString() : "";

            Response response = parseResponseFromDataXml(dataXml);
            return responseMapper.toDTO(response);
        } catch (ExternalServiceException ex){
            throw ex;
        } catch (Exception ex) {
            log.error("Ошибка при обработке запроса getPersonFlByIIN", ex);
            throw new ExternalServiceException("Внутренняя ошибка при вызове внешнего сервиса", ex);
        }

    }

    public String wrapRequestChildrenInData(Request request) {
        try {
            Document sourceDoc = marshalToDocument(request);
            Document resultDoc = extractChildrenUnderNewRoot(sourceDoc, "data");
            return documentToString(resultDoc);
        } catch (Exception ex) {
            throw new RuntimeException("Ошибка при оборачивании XML в <data>", ex);
        }

    }

    public Document marshalToDocument(Request request) {
        try {
            ObjectFactory factory = new ObjectFactory();
            JAXBElement<Request> jaxbElement = factory.createRequest(request);
            JAXBContext jaxbContext = JAXBContext.newInstance(Request.class.getPackage().getName());
            Marshaller marshaller = jaxbContext.createMarshaller();

            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = db.newDocument();

            marshaller.marshal(jaxbElement, document);
            return document;
        } catch (Exception ex) {
            throw new RuntimeException("Ошибка маршалинга Request в XML Document", ex);
        }
    }

    public Document extractChildrenUnderNewRoot(Document sourceDoc, String newRootTag) {
        try {
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document newDoc = db.newDocument();

            Element newRoot = newDoc.createElement(newRootTag);

            NodeList children = sourceDoc.getDocumentElement().getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                Node imported = newDoc.importNode(children.item(i), true);
                newRoot.appendChild(imported);
            }

            newDoc.appendChild(newRoot);
            return newDoc;
        } catch (ParserConfigurationException ex) {
            throw new RuntimeException("Ошибка при создании нового XML документа с корневым тегом " + newRootTag, ex);
        }

    }

    public String documentToString(Document doc) {
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(doc), new StreamResult(writer));
            return writer.toString();
        } catch (Exception ex) {
            throw new RuntimeException("Ошибка при преобразовании XML документа в строку", ex);
        }

    }

    public Response parseResponseFromDataXml(String dataXml) {
        try {
            Document dataDoc = parseXmlToDocument(dataXml);
            Element dataElement = dataDoc.getDocumentElement();

            Document responseDoc = wrapDataChildrenInResponse(dataElement);
            return unmarshalResponseFromDocument(responseDoc);
        } catch (Exception ex) {
            throw new RuntimeException("Ошибка при парсинге XML ответа в Response", ex);
        }

    }

    public Document parseXmlToDocument(String xml) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.parse(new InputSource(new StringReader(xml)));
        } catch (ParserConfigurationException | SAXException | IOException ex){
            throw new RuntimeException("Ошибка при парсинге XML в Document", ex);
        }
    }

    public Document wrapDataChildrenInResponse(Element dataElement) {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document newDoc = builder.newDocument();

            Element responseRoot = newDoc.createElement("data");
            NodeList children = dataElement.getChildNodes();

            for (int i = 0; i < children.getLength(); i++) {
                Node child = children.item(i);
                if (child.getNodeType() == Node.ELEMENT_NODE) {
                    Node imported = newDoc.importNode(child, true);
                    responseRoot.appendChild(imported);
                }
            }

            newDoc.appendChild(responseRoot);
            return newDoc;
        } catch (ParserConfigurationException ex) {
            throw new RuntimeException("Ошибка при создании документа DOM в wrapDataChildrenInResponse", ex);
        }
    }

    public Response unmarshalResponseFromDocument(Document responseDoc) {
        try {
            JAXBContext context = JAXBContext.newInstance(Response.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            return (Response) unmarshaller.unmarshal(responseDoc.getDocumentElement());
        } catch (Exception ex){
            throw new RuntimeException("Ошибка при анмаршалинге Response из XML Document", ex);
        }

    }

}
