import java.io.ByteArrayOutputStream;
import java.io.File;

import java.io.FileOutputStream;
import java.io.OutputStream;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;

import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;


public class ConvertXMLtoPDF {
    public static void main(String[] args) {
        ConvertXMLtoPDF convertXMLtoPDF = new ConvertXMLtoPDF();
        System.out.println("Convert and XML file to PDF using style sheet");
        
        File baseDir = new File(".");
       // File xmlFile = new File(baseDir,"TK2017M666002_2109378.xml");
        File xmlFile = new File(baseDir,"projectteam.xml");
       // File xsltFile = new File(baseDir,"simpleTrafficTicket.xsl");
        File xsltFile = new File(baseDir,"projectteam2fo.xsl");
        File pdfFile = new File("TrafficTicket_666002R.pdf");
        
        System.out.println("Absolute Path: "+ pdfFile.getAbsolutePath());
        System.out.println("Path: "+ pdfFile.getPath());
        System.out.println("Name: "+ pdfFile.getName());

        
        
        System.out.println(writeXMLtoPDF(xmlFile, pdfFile, xsltFile));
        ByteArrayOutputStream byteResult = new ByteArrayOutputStream();
        byteResult = getByteArrayPDF(xmlFile, pdfFile, xsltFile);
        
        byte[] b = byteResult.toByteArray();
        String pdfString = new String(b);
        // Just dump out for testing.
       // System.out.println("pdfString is "+pdfString);

        try{
        OutputStream filePDF = new java.io.FileOutputStream("test.pdf");
            filePDF.write(b);
        }catch(Exception exp){
            System.out.println(exp.getMessage());
        }
        
        
    }
    
    
    public static ByteArrayOutputStream getByteArrayPDF(File xmlFile, File pdfFile, File xsltFile){
    
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
       
        try{
            FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());
            FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
            Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, byteOut);

            TransformerFactory transFactory = TransformerFactory.newInstance();
            Transformer transformer = transFactory.newTransformer(new StreamSource(xsltFile));
            transformer.setParameter("versionParam", "1.0");
            Source src = new StreamSource(xmlFile);            
            Result res = new SAXResult(fop.getDefaultHandler());
            transformer.transform(src,res);
            System.out.println("Transform Completed Successfully");
            
        }catch(Exception exp){
            System.out.println(exp.getMessage());
        }
        return byteOut;
    }
    
    
    public static String writeXMLtoPDF(File xmlFile, File pdfFile, File xsltFile){
        String status = "Success";
        
        try{
            
            OutputStream outPDF = new java.io.FileOutputStream(pdfFile);
            outPDF = new java.io.BufferedOutputStream(outPDF);
          
            final FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());
            FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
            Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, outPDF);
            
            TransformerFactory transFactory = new org.apache.xalan.processor.TransformerFactoryImpl();
            
            //TransformerFactory transFactory = TransformerFactory.newInstance();
            Transformer transformer = transFactory.newTransformer(new StreamSource(xsltFile));
            transformer.setParameter("versionParam", "1.0");
            Source src = new StreamSource(xmlFile);
            Result res = new SAXResult(fop.getDefaultHandler());
            transformer.transform(src,res);
            
            
           outPDF.close();
           System.out.println("File created");
            
        }catch(Exception exp){
            status="Failed";
            exp.printStackTrace(System.err);
        }        
        return status;
    }
    
}
