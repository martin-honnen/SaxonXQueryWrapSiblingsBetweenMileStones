package org.example;

import net.sf.saxon.option.jdom2.JDOM2ObjectModel;
import net.sf.saxon.s9api.*;
import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.jdom2.transform.JDOMResult;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, SaxonApiException, JDOMException {
        Processor processor = new Processor();

        processor.getUnderlyingConfiguration().registerExternalObjectModel(new JDOM2ObjectModel());

        Document jdomDocument = new SAXBuilder().build(new File("sample1.xml"));

        DocumentBuilder docBuilder = processor.newDocumentBuilder();

        XdmNode inputDoc = docBuilder.wrap(jdomDocument);

        XQueryCompiler xqueryCompiler = processor.newXQueryCompiler();

        XQueryExecutable xqueryExecutable = xqueryCompiler.compile(new File("wrap-siblings-between-milestones1.xq"));

        XQueryEvaluator xqueryEvaluator = xqueryExecutable.load();

        xqueryEvaluator.setContextItem(inputDoc);

        JDOMResult jdomResult = new JDOMResult();

        xqueryEvaluator.run(new SAXDestination(jdomResult.getHandler()));

        Document resultDoc = jdomResult.getDocument();

        XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());

        xmlOutputter.output(resultDoc, System.out);

    }
}