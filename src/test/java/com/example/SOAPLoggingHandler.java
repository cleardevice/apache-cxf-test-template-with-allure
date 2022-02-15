package com.example;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.io.ByteArrayOutputStream;
import java.util.Set;


/**
 * This simple SOAPHandler will output the contents of incoming
 * and outgoing messages.
 */
public class SOAPLoggingHandler implements SOAPHandler<SOAPMessageContext> {

    private final ByteArrayOutputStream request;
    private final ByteArrayOutputStream response;

    SOAPLoggingHandler(ByteArrayOutputStream request, ByteArrayOutputStream response) {
        this.request = request;
        this.response = response;
    }

    @Override
    public Set<QName> getHeaders() {
        return null;
    }

    @Override
    public boolean handleMessage(SOAPMessageContext smc) {
        logToAllure(smc);
        return true;
    }

    @Override
    public boolean handleFault(SOAPMessageContext smc) {
        logToAllure(smc);
        return true;
    }

    @Override
    public void close(MessageContext messageContext) {
    }

    /**
     * Check the MESSAGE_OUTBOUND_PROPERTY in the context
     * to see if this is an outgoing or incoming message.
     * Write a brief message to the print stream and
     * output the message. The writeTo() method can throw
     * SOAPException or IOException
     **/
    private void logToAllure(SOAPMessageContext smc) {
        boolean isThisIsRequestAction = Boolean.parseBoolean(String.valueOf(smc.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY)));
        SOAPMessage message = smc.getMessage();

        try {
            final TransformerFactory transformerFactory = TransformerFactory.newInstance();
            final Transformer transformer = transformerFactory.newTransformer();
            // Format it
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            final ByteArrayOutputStream streamOut = new ByteArrayOutputStream();
            final StreamResult result = new StreamResult(streamOut);
            transformer.transform(message.getSOAPPart().getContent(), result);

            if (isThisIsRequestAction) {
                request.write(streamOut.toByteArray());
            } else {
                response.write(streamOut.toByteArray());
            }
        } catch (Exception e) {
            System.out.println("Couldn't provide log data for Allure:" + e);
        }
    }
}