
package org.bumblescript.jfast;

import java.io.*;


public class JFastOutput extends OutputStream
{
    private OutputStream output;
    private int requestId;
    
    public JFastOutput(OutputStream output,int requestId)
    {
        this.output = output;
        this.requestId = requestId;
    }
    
    public void write(byte buffer[],int offset,int length)
        throws IOException
    {    
        for (int o = offset, l = Math.min(length, JFast.MAX_LENGTH);
	     l != 0 && o + l <= offset + length;
	     o += l, l = Math.min(offset + length - o, JFast.MAX_LENGTH))
	{
	    // wrap our data in a JFastMessage and send it back
	    // to the server.
	    byte content[];
	    if(o == 0 && l == buffer.length)
	    {
		content = buffer;
	    }
	    else
	    {
		content = new byte[l];
		System.arraycopy(buffer,o,content,0,l);
	    }
        
	    // wrap data in a message and then send it
	    JFastMessage message = new JFastMessage(JFast.VERSION,
                getMessageType(),requestId);
	    message.setContent(content);
	    message.write(output);
	}
    }
    
    public void write(byte buffer[])
        throws IOException
    {
        write(buffer,0,buffer.length);
    }
    
    public void write(int character)
        throws IOException
    {
        // this shouldn't ever be called, but if it is, we'll 
        // grotesquely send a single character, wrapped in an
        // entire JFastMessage. The alternative is to throw and
        // exception, denying the abstract requirements of this class,
        // and who wants that?
        byte buffer[] = new byte[1];
        buffer[0] = (byte) character;
        write(buffer);
    }
    
    private int getMessageType()     
    {
        return JFast.STDOUT;
    }  
}
