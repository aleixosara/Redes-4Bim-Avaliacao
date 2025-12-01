package T4;

import java.util.Scanner;

public class Main {
    final static Scanner LER = new Scanner(System.in);

    public static void main(String[] args) {
        String entrada = LER.nextLine();

        String[] ipMask = entrada.split("/");
        if (ipMask.length != 2) {
            System.out.println("Endereço IP inválido");
            return;
        }

        String ip = ipMask[0];
        int mascara = Integer.parseInt(ipMask[1]);

        String[] partes = ip.split("\\.");
        Boolean ipValido;
        String endRede;
        String broadcast;
        String faixaInicio;
        String faixaFim;

        ipValido = validateIp(ip, partes);

        if (ipValido == true) {

            endRede = fetchEndRede(mascara, partes);
            broadcast = fetchBroadcast(mascara, partes);
            faixaInicio = fetchFaixaInicio(mascara, partes);
            faixaFim = fetchFaixaFim(mascara, partes);

            print(endRede, broadcast, faixaInicio, faixaFim);
        } else {
            System.out.println("Endereço IP inválido");
        }
    }

    public static boolean validateIp(String ip, String[] partes) {
        if (partes.length != 4) {
            return false;
        }

        for (String parte : partes) {
            int n = Integer.parseInt(parte);
            if (n < 0 || n > 255) {
                return false;
            }
        }

        return true;
    }

    public static String ipToBin(String[] partes) {
        return String.format("%8s", Integer.toBinaryString(Integer.parseInt(partes[0]))).replace(' ', '0') +
                String.format("%8s", Integer.toBinaryString(Integer.parseInt(partes[1]))).replace(' ', '0') +
                String.format("%8s", Integer.toBinaryString(Integer.parseInt(partes[2]))).replace(' ', '0') +
                String.format("%8s", Integer.toBinaryString(Integer.parseInt(partes[3]))).replace(' ', '0');
    }

    public static String maskToBin(int mask) {
        return "1".repeat(mask) + "0".repeat(32 - mask);
    }

    public static String binToIp(String bin) {
        int o1 = Integer.parseInt(bin.substring(0, 8), 2);
        int o2 = Integer.parseInt(bin.substring(8, 16), 2);
        int o3 = Integer.parseInt(bin.substring(16, 24), 2);
        int o4 = Integer.parseInt(bin.substring(24, 32), 2);

        return o1 + "." + o2 + "." + o3 + "." + o4;
    }

    public static String fetchEndRede(int mascara, String[] partes) {
        String ipBin = ipToBin(partes);
        String maskBin = maskToBin(mascara);

        String rede = "";
        for (int i = 0; i < 32; i++) {
            rede += (ipBin.charAt(i) == '1' && maskBin.charAt(i) == '1') ? "1" : "0";
        }

        return binToIp(rede);
    }

    public static String fetchBroadcast(int mascara, String[] partes) {
        String rede = ipToBin(partes);
        rede = fetchEndRede(mascara, partes);
        String redeBin = ipToBin(rede.split("\\."));

        String maskBin = maskToBin(mascara);

        String broad = "";
        for (int i = 0; i < 32; i++) {
            broad += (maskBin.charAt(i) == '1') ? redeBin.charAt(i) : "1";
        }

        return binToIp(broad);
    }

    public static String fetchFaixaInicio(int mascara, String[] partes) {
        String rede = fetchEndRede(mascara, partes);
        String[] r = rede.split("\\.");

        return r[0] + "." + r[1] + "." + r[2] + "." + (Integer.parseInt(r[3]) + 1);
    }

    public static String fetchFaixaFim(int mascara, String[] partes) {
        String broad = fetchBroadcast(mascara, partes);
        String[] b = broad.split("\\.");

        return b[0] + "." + b[1] + "." + b[2] + "." + (Integer.parseInt(b[3]) - 1);
    }

    public static void print(String endRede, String broadcast, String faixaInicio, String faixaFim) {
        System.out.println("Rede: " + endRede);
        System.out.println("Broadcast: " + broadcast);
        System.out.println("Hosts: de " + faixaInicio + " a " + faixaFim);
    }
}