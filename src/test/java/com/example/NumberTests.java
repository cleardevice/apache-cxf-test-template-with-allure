package com.example;

import com.dataaccess.webservicesserver.NumberConversion;
import com.dataaccess.webservicesserver.NumberConversionSoapType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

public class NumberTests {

    private final ByteArrayOutputStream request = new ByteArrayOutputStream();
    private final ByteArrayOutputStream response = new ByteArrayOutputStream();

    @RegisterExtension
    LoggingExtension staticExtension = new LoggingExtension(request, response);

    private NumberConversionSoapType getPort() {
        NumberConversion service = new NumberConversion();
        NumberConversionSoapType numberConversionSoap = service.getNumberConversionSoap12();

        Binding binding = ((BindingProvider) numberConversionSoap).getBinding();
        List<Handler> handlerChain = binding.getHandlerChain();
        handlerChain.add(new SOAPLoggingHandler(request, response));
        binding.setHandlerChain(handlerChain);

        return numberConversionSoap;
    }

    @Test
    public void testNumberConversion1() {
        String result = getPort().numberToWords(BigInteger.valueOf(1234));
        Assertions.assertEquals(result, "one thousand two hundred and thirty four");
    }

    @Test
    public void testNumberConversion2() {
        String result = getPort().numberToDollars(BigDecimal.valueOf(123.45));
        Assertions.assertEquals(result, "one hundred and twenty three dollars and forty five cents");
    }
}