package fi.iki.hsivonen.verifierservlet;

import java.io.IOException;

import org.whattf.checker.NormalizationChecker;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.ibm.icu.text.Normalizer;

import fi.karppinen.xml.CharacterUtil;

public abstract class AbstractErrorHandler implements InfoErrorHandler {

    private int warnings = 0;

    private int errors = 0;

    private int fatalErrors = 0;

    protected static String scrub(String s) throws SAXException {
        s = CharacterUtil.prudentlyScrubCharacterData(s);
        if (NormalizationChecker.startsWithComposingChar(s)) {
            s = " " + s;
        }
        return Normalizer.normalize(s, Normalizer.NFC, 0);
    }

    public AbstractErrorHandler() {
        super();
    }

    /**
     * @return Returns the errors.
     */
    public final int getErrors() {
        return errors;
    }

    /**
     * @return Returns the fatalErrors.
     */
    public final int getFatalErrors() {
        return fatalErrors;
    }

    /**
     * @return Returns the warnings.
     */
    public final int getWarnings() {
        return warnings;
    }

    public final boolean isErrors() {
        return !(errors == 0 && fatalErrors == 0);
    }

    /**
     * @see org.xml.sax.ErrorHandler#warning(org.xml.sax.SAXParseException)
     */
    public final void warning(SAXParseException e) throws SAXException {
        this.warnings++;
        warningImpl(e);
    }

    /**
     * @see org.xml.sax.ErrorHandler#error(org.xml.sax.SAXParseException)
     */
    public final void error(SAXParseException e) throws SAXException {
        this.errors++;
        errorImpl(e);
    }

    /**
     * @see org.xml.sax.ErrorHandler#fatalError(org.xml.sax.SAXParseException)
     */
    public final void fatalError(SAXParseException e) throws SAXException {
        this.fatalErrors++;
        Exception wrapped = e.getException();
        if (wrapped instanceof IOException) {
            ioErrorImpl((IOException) wrapped);
        } else {
            fatalErrorImpl(e);
        }
    }

    /**
     * @see fi.iki.hsivonen.verifierservlet.InfoErrorHandler#info(java.lang.String)
     */
    public final void info(String str) throws SAXException {
        infoImpl(str);
    }

    /**
     * @see fi.iki.hsivonen.verifierservlet.InfoErrorHandler#ioError(java.io.IOException)
     */
    public final void ioError(IOException e) throws SAXException {
        this.fatalErrors++;
        ioErrorImpl(e);
    }

    /**
     * @see fi.iki.hsivonen.verifierservlet.InfoErrorHandler#internalError(java.lang.Throwable,
     *      java.lang.String)
     */
    public final void internalError(Throwable e, String message) throws SAXException {
        this.fatalErrors++;
        internalErrorImpl(message);
    }

    /**
     * @see fi.iki.hsivonen.verifierservlet.InfoErrorHandler#schemaError(java.lang.Exception)
     */
    public final void schemaError(Exception e) throws SAXException {
        this.fatalErrors++;
        schemaErrorImpl(e);
    }

    /**
     * @param e
     * @throws SAXException
     */
    protected abstract void warningImpl(SAXParseException e)
            throws SAXException;

    /**
     * @param e
     * @throws SAXException
     */
    protected abstract void errorImpl(SAXParseException e) throws SAXException;

    /**
     * @param e
     * @throws SAXException
     */
    protected abstract void fatalErrorImpl(SAXParseException e)
            throws SAXException;



    /**
     * @param str
     * @throws SAXException
     */
    protected abstract void infoImpl(String str) throws SAXException;

    /**
     * @param e
     * @throws SAXException
     */
    protected abstract void ioErrorImpl(IOException e) throws SAXException;

    /**
     * @param message
     * @throws SAXException
     */
    protected abstract void internalErrorImpl(String message)
            throws SAXException;

    /**
     * @param e
     * @throws SAXException
     */
    protected abstract void schemaErrorImpl(Exception e) throws SAXException;

    /**
     * @see fi.iki.hsivonen.verifierservlet.InfoErrorHandler#start()
     */
    public void start(String documentUri) throws SAXException {

    }
    
    /**
     * @see fi.iki.hsivonen.verifierservlet.InfoErrorHandler#end()
     */
    public void end(String successMessage, String failureMessage)
            throws SAXException {

    }
    
}