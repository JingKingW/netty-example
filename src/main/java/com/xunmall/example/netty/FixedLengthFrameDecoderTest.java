package com.xunmall.example.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

import static org.junit.Assert.*;
/**
 * @author WangYanjing
 * @description
 * @date 2020/8/24 18:02
 */
public class FixedLengthFrameDecoderTest {

    @Test
    public void testFrameDecoder() throws IllegalAccessException {
        ByteBuf buf = Unpooled.buffer();
        for (int i = 0; i < 9; i++){
            buf.writeByte(i);
        }
        ByteBuf input = buf.duplicate();
        EmbeddedChannel channel = new EmbeddedChannel(new FixedLengthFrameDecoder(3));
        assertTrue(channel.writeInbound(input.retain()));
        assertTrue(channel.finish());

        // read messages
        ByteBuf read = (ByteBuf) channel.readInbound();
        assertEquals(buf.readSlice(3),  read);
        read.release();

        read = (ByteBuf) channel.readInbound();
        assertEquals(buf.readSlice(3),  read);
        read.release();

        read = (ByteBuf) channel.readInbound();
        assertEquals(buf.readSlice(3),  read);
        read.release();

        assertNull(channel.readInbound());
        buf.release();
    }


}
