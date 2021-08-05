package me.matchers.bot.manager;
/*/
Created by Matchers on 4/14/2021
 */

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.Charset;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import com.eclipsesource.json.JsonObject;

public class ServerHandler {

    /*/
    This util was copied from spigotmc.org.
     */
    public static String playerCount(String host, int port) throws IOException {
        try (Socket socket = new Socket(host, port)) {
            try (DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {
                try (DataInputStream in = new DataInputStream(socket.getInputStream())) {
                    try (ByteArrayOutputStream frame = new ByteArrayOutputStream()) {
                        try (DataOutputStream frameOut = new DataOutputStream(frame)) {

                            // Handshake
                            writeVarInt(0x00, frameOut);
                            writeVarInt(4, frameOut);
                            writeString(host, frameOut);
                            frameOut.writeShort(port);
                            writeVarInt(1, frameOut);
                            // Write handshake
                            writeVarInt(frame.size(), out);
                            frame.writeTo(out);
                            frame.reset();

                            // Ping
                            writeVarInt(0x00, frameOut);
                            // Write ping
                            writeVarInt(frame.size(), out);
                            frame.writeTo(out);
                            frame.reset();

                            int len = readVarInt(in);
                            byte[] packet = new byte[len];
                            in.readFully(packet);

                            try (ByteArrayInputStream inPacket = new ByteArrayInputStream(packet)) {
                                try (DataInputStream inFrame = new DataInputStream(inPacket)) {
                                    int id = readVarInt(inFrame);
                                    if (id != 0x00) {
                                        throw new IllegalStateException("Wrong ping response");
                                    }

                                    JsonObject jsonObject = JsonObject.readFrom(readString(inFrame));

                                    JsonObject jsonPlayers = jsonObject.get("players").asObject();

                                    return jsonPlayers.get("online").asInt() + "/" + jsonPlayers.get("max").asInt();
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error/Error";
        }
    }

    public static void writeString(String s, DataOutput out) throws IOException {
        // TODO: Check len - use Guava?
        byte[] b = s.getBytes("UTF-8");
        writeVarInt(b.length, out);
        out.write(b);
    }

    public static String readString(DataInput in) throws IOException {
        int len = readVarInt(in);
        byte[] b = new byte[len];
        in.readFully(b);

        return new String(b, "UTF-8");
    }

    public static int readVarInt(DataInput input) throws IOException {
        int out = 0;
        int bytes = 0;
        byte in;
        while (true) {
            in = input.readByte();

            out |= (in & 0x7F) << (bytes++ * 7);

            if (bytes > 32) {
                throw new RuntimeException("VarInt too big");
            }

            if ((in & 0x80) != 0x80) {
                break;
            }
        }

        return out;
    }

    public static void writeVarInt(int value, DataOutput output) throws IOException {
        int part;
        while (true) {
            part = value & 0x7F;

            value >>>= 7;
            if (value != 0) {
                part |= 0x80;
            }

            output.writeByte(part);

            if (value == 0) {
                break;
            }
        }
    }
}
