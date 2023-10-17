/**
 * 
 */
package gov.naco.soch.admin.captcha;

import java.security.SecureRandom;
import java.util.Random;
import nl.captcha.text.producer.TextProducer;

/**
 * @author Pranav MS (144958)
 * @email pranav.sasi@ust-global.com
 * @date 2021-Jun-25 5:29:16 pm 
 * 
 */
public class CaptchaTextProducerConfig implements TextProducer {
	private static final Random RAND = new SecureRandom();
    private static final int DEFAULT_LENGTH = 6;
    private static final char[] DEFAULT_CHARS = new char[] { 'a', 'b', 'c', 'd',
            'e', 'f', 'g', 'h', 'k', 'm', 'n', 'p', 'r', 'w', 'x', 'y',
            '2', '3', '4', '5', '6', '7', '8', };
    
    private final int _length;
    private final char[] _srcChars;

    public CaptchaTextProducerConfig() {
    	this(DEFAULT_LENGTH, DEFAULT_CHARS);
    }
    
    public CaptchaTextProducerConfig(int length, char[] srcChars) {
    	_length = length;
    	_srcChars = copyOf(srcChars, srcChars.length);
    }
    
    @Override
    public String getText() {
        String capText = "";
        for (int i = 0; i < _length; i++) {
            capText += _srcChars[RAND.nextInt(_srcChars.length)];
        }

        return capText;
    }
    
    private static char[] copyOf(char[] original, int newLength) {
        char[] copy = new char[newLength];
        System.arraycopy(original, 0, copy, 0,
                Math.min(original.length, newLength));
        return copy;
    }
}
