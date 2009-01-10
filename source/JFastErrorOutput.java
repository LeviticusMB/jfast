
package org.bumblescript.jfast;

import java.io.*;


public class JFastErrorOutput extends JFastOutput
{  
    public JFastErrorOutput(OutputStream output,int requestId)
    {
        super(output,requestId);
    }
    
    private int getMessageType()     
    {
        return JFast.STDERR;
    }
}
