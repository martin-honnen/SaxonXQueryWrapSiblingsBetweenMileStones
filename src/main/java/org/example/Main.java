package org.example;

import net.sf.saxon.s9api.*;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, SaxonApiException {
        Processor processor = new Processor();

        DocumentBuilder docBuilder = processor.newDocumentBuilder();

        XdmNode inputDoc = docBuilder.build(new File("sample1.xml"));

        XQueryCompiler xqueryCompiler = processor.newXQueryCompiler();

        XQueryExecutable xqueryExecutable = xqueryCompiler.compile(new File("wrap-siblings-between-milestones1.xq"));

        XQueryEvaluator xqueryEvaluator = xqueryExecutable.load();

        xqueryEvaluator.setContextItem(inputDoc);

        xqueryEvaluator.run(processor.newSerializer(System.out));

    }
}