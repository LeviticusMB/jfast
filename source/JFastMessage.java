
package org.bumblescript.jfast;

import java.io.*;


public class JFastMessage
{
    public int version;
    public int type;
    public int requestId;
    public int contentLength;
    public int paddingLength;
    public byte[] content;
    public byte[] padding;
                   
    
    public JFastMessage(InputStream input)
        throws IOException, JFastException
    {
        // read header
        byte header[] = new byte[JFast.HEADER_LENGTH];
        if(input.read(header,0,JFast.HEADER_LENGTH) != JFast.HEADER_LENGTH)
        {
            throw new JFastException("Invalid header length");
        }
        
        version = header[0] & 0xff;
        type = header[1] & 0xff;
        requestId = ((header[2] & 0xff) << 8) | (header[3] & 0xff);
        contentLength = ((header[4] & 0xff) << 8) | (header[5] & 0xff);
        paddingLength = header[6] & 0xff;
        
        // read data
        content = new byte[contentLength];
        padding = new byte[paddingLength];
        if(input.read(content,0,contentLength) != contentLength ||
            input.read(padding,0,paddingLength) != paddingLength)
        {
            throw new JFastException("Invalid data length");
        }
    }    
    
    public JFastMessage(int version,int type,int requestId)
    {
        this.version = version;
        this.type = type;
        this.requestId = requestId;
        this.contentLength = 0;
        this.content = new byte[this.contentLength];
        this.paddingLength = 0;
        this.padding = new byte[this.paddingLength];
    }
    
    public void setContent(byte content[])
    {
        this.content = content;
        this.contentLength = content.length;
    }
    
    public void write(OutputStream output)
        throws IOException
    {
        byte header[] = new byte[JFast.HEADER_LENGTH];
        header[0] = (byte) JFast.VERSION;
        header[1] = (byte) type;
        header[2] = (byte) ((requestId >> 8) & 0xff);
        header[3] = (byte) (requestId & 0xff);
        header[4] = (byte) ((contentLength >> 8) & 0xff);
        header[5] = (byte) (contentLength & 0xff);
        header[6] = (byte) paddingLength;
        header[7] = 0;
         
        output.write(header);        
        output.write(content);
    }
}
