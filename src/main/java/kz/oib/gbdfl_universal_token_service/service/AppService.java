package kz.oib.gbdfl_universal_token_service.service;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import kz.bee.bip.syncchannel.v10.types.response.SyncSendMessageResponse;
import kz.oib.common.model.ServiceReturn;
import kz.oib.common.util.ShepConnectorRequestUtil;
import kz.oib.gbdfl_universal_token_service.config.AppConstants;
import kz.oib.gbdfl_universal_token_service.model.dto.RequestDTO;
import kz.oib.gbdfl_universal_token_service.model.dto.ResponseDTO;
import kz.oib.gbdfl_universal_token_service.model.mapper.RequestMapper;
import kz.oib.gbdfl_universal_token_service.model.mapper.ResponseMapper;
import kz.oib.gbdfl_universal_token_service.xsd.RequestAndResponse.Request;
import kz.oib.gbdfl_universal_token_service.xsd.RequestAndResponse.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringReader;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.UUID;

import static kz.oib.common.util.MarshallerUtil.convertServiceReturn;

@Service
@RequiredArgsConstructor
public class AppService {
    private final AppConstants appConstants;
    private final RequestMapper requestMapper;
    private final ResponseMapper responseMapper;
    private final RestTemplate restTemplate;
    private final Environment env;


    public ResponseDTO getPersonFlByIIN(String iin) throws Exception {
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
                .channelType(channelType)
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
            System.out.println("Пустой ответ от ШЭП: serviceReturn или returnMessage == null");
            return null;
        }

        SyncSendMessageResponse syncResp = convertServiceReturn(serviceReturn, SyncSendMessageResponse.class);
        Object responseData = syncResp.getResponseData().getData();
        String dataXml = (data != null) ? data.toString() : "";

        Response response = parseResponseFromDataXml(dataXml);
        return responseMapper.toDTO(response);
    }

    public static String wrapRequestChildrenInData(Request request) throws Exception {
        Document sourceDoc = marshalToDocument(request);
        Document resultDoc = extractChildrenUnderNewRoot(sourceDoc, "data");
        return documentToString(resultDoc);
    }

    public static Document marshalToDocument(Object object) throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(object.getClass());
        Marshaller marshaller = jaxbContext.createMarshaller();

        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = db.newDocument();

        marshaller.marshal(object, document);
        return document;
    }

    public static Document extractChildrenUnderNewRoot(Document sourceDoc, String newRootTag) throws Exception {
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
    }

    public static String documentToString(Document doc) throws Exception {
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(doc), new StreamResult(writer));
        return writer.toString();
    }

    public static Response parseResponseFromDataXml(String dataXml) throws Exception {
        Document dataDoc = parseXmlToDocument(dataXml);
        Element dataElement = dataDoc.getDocumentElement();

        Document responseDoc = wrapDataChildrenInResponse(dataElement);
        return unmarshalResponseFromDocument(responseDoc);
    }

    private static Document parseXmlToDocument(String xml) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(new InputSource(new StringReader(xml)));
    }

    private static Document wrapDataChildrenInResponse(Element dataElement) throws Exception {
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
    }

    private static Response unmarshalResponseFromDocument(Document responseDoc) throws Exception {
        JAXBContext context = JAXBContext.newInstance(Response.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return (Response) unmarshaller.unmarshal(responseDoc.getDocumentElement());
    }

}
