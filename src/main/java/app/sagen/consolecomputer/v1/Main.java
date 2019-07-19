package app.sagen.consolecomputer.v1;

public class Main {

    public static int[] loadAdditionAndCountProgram() {
        int[] memory = new int[0xFF];
        memory[0] = ConsoleComputer.LOAD;
        memory[1] = 254;
        memory[2] = ConsoleComputer.IN;
        memory[3] = ConsoleComputer.ADD;
        memory[4] = 254;
        memory[5] = ConsoleComputer.IN;
        memory[6] = ConsoleComputer.SUBI;
        memory[7] = 1;
        memory[8] = ConsoleComputer.JGZ;
        memory[9] = 4;
        memory[19] = ConsoleComputer.HALT;
        memory[254] = 5;
        return memory;
    }

    public static int[] load4FactorialProgram() {
        int[] memory = new int[0xFF];
        memory[0] = 0x02;
        memory[1] = 32;
        memory[2] = 0x01;
        memory[3] = 31;
        memory[4] = 0x02;
        memory[5] = 30;
        memory[6] = 0x07;
        memory[7] = 31;
        memory[8] = 0x01;
        memory[9] = 31;
        memory[10] = 0x02;
        memory[11] = 30;
        memory[12] = 0x04;
        memory[13] = 1;
        memory[14] = 0x01;
        memory[15] = 30;
        memory[16] = 0x0B;
        memory[17] = 6;
        memory[18] = 0x02;
        memory[19] = 31;
        memory[20] = 0x0A;
        memory[21] = 0x00;
        memory[30] = 4;
        memory[31] = 0;
        memory[32] = 1;
        return memory;
    }

    public static int[] loadHelloWorld() {
        int[] memory = new int[0xFF];
        memory[0] = ConsoleComputer.LOADPTR;
        memory[1] = 99;
        memory[2] = ConsoleComputer.JZERO;
        memory[3] = 15;
        memory[4] = ConsoleComputer.PRTCHR;
        memory[5] = ConsoleComputer.LOAD;
        memory[6] = 99;
        memory[7] = ConsoleComputer.ADDI;
        memory[8] = 1;
        memory[9] = ConsoleComputer.STORE;
        memory[10] = 99;
        memory[11] = ConsoleComputer.LOADPTR;
        memory[12] = 99;
        memory[13] = ConsoleComputer.JGZ;
        memory[14] = 2;
        memory[15] = ConsoleComputer.HALT;
        memory[99] = 100;
        memory[100] = 'H';
        memory[101] = 'e';
        memory[102] = 'l';
        memory[103] = 'l';
        memory[104] = 'o';
        memory[105] = ',';
        memory[106] = ' ';
        memory[107] = 'W';
        memory[108] = 'o';
        memory[109] = 'r';
        memory[110] = 'l';
        memory[111] = 'd';
        memory[112] = '\n';
        memory[113] = 0x00;  // zero terminating string
        return memory;
    }


    public static void main(String...args) {
        ConsoleComputer consoleComputer = new ConsoleComputer(load4FactorialProgram());
        while(consoleComputer.executeStep());
    }

}
