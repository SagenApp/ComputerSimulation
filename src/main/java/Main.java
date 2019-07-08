public class Main {

    public static short[] loadAdditionAndCountProgram() {
        short[] memory = new short[0xFF];
        memory[0] = 0x02;   // LOAD
        memory[1] = 254;    // [254]
        memory[2] = 0X09;   // IN
        memory[3] = 0X05;   // ADD
        memory[4] = 254;    // [254]
        memory[5] = 0X0A;   // IN
        memory[6] = 0X04;   // SUBI
        memory[7] = 1;      // 1
        memory[8] = 0x0B;   // JGZ
        memory[9] = 4;      // 4
        memory[19] = 0x00;  // HALT
        memory[254] = 5;    // 5
        return memory;
    }

    public static short[] load4FactorialProgram() {
        short[] memory = new short[0xFF];
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

    public static void main(String...args) {
        ConsoleComputer consoleComputer = new ConsoleComputer(load4FactorialProgram());
        while(consoleComputer.executeStep());
    }

}
