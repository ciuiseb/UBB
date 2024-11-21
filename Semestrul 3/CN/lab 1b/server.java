import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.Arrays;

class Server {
   public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(1234)) {
            System.out.println("Server is listening on port 1234...");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("A client has connected.");
                Thread clientThread = new Thread(() -> handleClient(clientSocket));
                clientThread.start();
            }
        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
        }
    }
	 private static void handleClient(Socket clientSocket) {
        try (
            DataInputStream in = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream())
        ) {
            while (!clientSocket.isClosed()) {
                try {
                    short exercise = in.readShort();
                    solveServer(exercise, in, out);
                } catch (EOFException e) {
                    break;
                } catch (IOException e) {
                    System.out.println("Error handling client request: " + e.getMessage());
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("Error handling client connection: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
                System.out.println("Client disconnected");
            } catch (IOException e) {
                System.out.println("Error closing client socket: " + e.getMessage());
            }
        }
    }
    private static void solveServer(short option, DataInputStream in, DataOutputStream out) throws IOException {
        if (option < 1 || option > 10) return;
        System.out.println("Solving problem number " + option);

        switch (option) {
            case 1: solveServer_1(in, out); break;
            case 2: solveServer_2(in, out); break;
            case 3: solveServer_3(in, out); break;
            case 4: solveServer_4(in, out); break;
            case 5: solveServer_5(in, out); break;
            case 6: solveServer_6(in, out); break;
            case 7: solveServer_7(in, out); break;
            case 8: solveServer_8(in, out); break;
            case 9: solveServer_9(in, out); break;
            case 10: solveServer_10(in, out); break;
        }
    }

    private static void solveServer_1(DataInputStream in, DataOutputStream out) throws IOException {
        short size = in.readShort();
        int sum = 0;

        for (int i = 0; i < size; i++) {
            sum += in.readShort();
        }

        out.writeShort(sum);
    }

    private static void solveServer_2(DataInputStream in, DataOutputStream out) throws IOException {
        byte[] buffer = new byte[256];
        in.readFully(buffer);

        int spaceCount = 0;
        for (byte b : buffer) {
            if (b == ' ') spaceCount++;
        }

        out.writeShort(spaceCount);
    }

    private static void solveServer_3(DataInputStream in, DataOutputStream out) throws IOException {
        byte[] buffer = new byte[256];
        in.readFully(buffer);

        int length = 0;
        while (length < buffer.length && buffer[length] != 0) length++;

        for (int i = 0; i < length / 2; i++) {
            byte temp = buffer[i];
            buffer[i] = buffer[length - 1 - i];
            buffer[length - 1 - i] = temp;
        }

        out.write(buffer, 0, length);
    }

    private static void solveServer_4(DataInputStream in, DataOutputStream out) throws IOException {
        short len1 = in.readShort();
        byte[] str1 = new byte[len1];
        in.readFully(str1);

        short len2 = in.readShort();
        byte[] str2 = new byte[len2];
        in.readFully(str2);

        byte[] result = new byte[len1 + len2];
        int i = 0, j = 0, k = 0;

        while (i < len1 && j < len2) {
            if (str1[i] < str2[j]) {
                result[k++] = str1[i++];
            } else {
                result[k++] = str2[j++];
            }
        }

        while (i < len1) result[k++] = str1[i++];
        while (j < len2) result[k++] = str2[j++];

        out.write(result, 0, k);
    }

    private static void solveServer_5(DataInputStream in, DataOutputStream out) throws IOException {
        short number = in.readShort();
        short[] divisors = new short[number];
        int count = 0;

        for (int i = 1; i <= number / 2; i++) {
            if (number % i == 0) {
                divisors[count++] = (short)i;
                if (i != number / i) {
                    divisors[count++] = (short)(number / i);
                }
            }
        }

        out.writeShort(count);
        for (int i = 0; i < count; i++) {
            out.writeShort(divisors[i]);
        }
    }

    private static void solveServer_6(DataInputStream in, DataOutputStream out) throws IOException {
        short length = in.readShort();
        byte[] buffer = new byte[length];
        in.readFully(buffer);
        byte target = in.readByte();

        short[] positions = new short[length];
        int count = 0;

        for (int i = 0; i < length; i++) {
            if (buffer[i] == target) {
                positions[count++] = (short)i;
            }
        }

        out.writeShort(count);
        for (int i = 0; i < count; i++) {
            out.writeShort(positions[i]);
        }
    }

    private static void solveServer_7(DataInputStream in, DataOutputStream out) throws IOException {
        short len = in.readShort();
        byte[] buffer = new byte[len];
        in.readFully(buffer);

        short startingPoint = in.readShort();
        short substringLength = in.readShort();

        out.write(buffer, startingPoint, substringLength);
    }

    private static void solveServer_8(DataInputStream in, DataOutputStream out) throws IOException {
        short len1 = in.readShort();
        int[] arr1 = new int[len1];
        for (int i = 0; i < len1; i++) arr1[i] = in.readInt();

        short len2 = in.readShort();
        int[] arr2 = new int[len2];
        for (int i = 0; i < len2; i++) arr2[i] = in.readInt();

        int[] common = new int[Math.min(len1, len2)];
        int commonCount = 0;

        for (int i = 0; i < len1; i++) {
            for (int j = 0; j < len2; j++) {
                if (arr1[i] == arr2[j]) {
                    common[commonCount++] = arr1[i];
                    break;
                }
            }
        }

        out.writeShort(commonCount);
        for (int i = 0; i < commonCount; i++) {
            out.writeInt(common[i]);
        }
    }

    private static void solveServer_9(DataInputStream in, DataOutputStream out) throws IOException {
        short len1 = in.readShort();
        int[] arr1 = new int[len1];
        for (int i = 0; i < len1; i++) arr1[i] = in.readInt();

        short len2 = in.readShort();
        int[] arr2 = new int[len2];
        for (int i = 0; i < len2; i++) arr2[i] = in.readInt();

        int[] result = new int[len1];
        int resultCount = 0;

        for (int value : arr1) {
            boolean found = false;
            for (int i = 0; i < len2; i++) {
                if (value == arr2[i]) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                result[resultCount++] = value;
            }
        }

        out.writeShort(resultCount);
        for (int i = 0; i < resultCount; i++) {
            out.writeInt(result[i]);
        }
    }

    private static void solveServer_10(DataInputStream in, DataOutputStream out) throws IOException {
        short len1 = in.readShort();
        byte[] str1 = new byte[len1];
        in.readFully(str1);

        short len2 = in.readShort();
        byte[] str2 = new byte[len2];
        in.readFully(str2);

        byte maxChar = 0;
        int maxCount = 0;

        for (int i = 0; i < len1 && i < len2; i++) {
            if (str1[i] == str2[i]) {
                int count = 1;
                for (int j = i + 1; j < len1 && j < len2; j++) {
                    if (str1[j] == str2[j] && str1[j] == str1[i]) {
                        count++;
                    }
                }
                if (count > maxCount) {
                    maxCount = count;
                    maxChar = str1[i];
                }
            }
        }

        out.writeByte(maxChar);
        out.writeShort(maxCount);
    }
}