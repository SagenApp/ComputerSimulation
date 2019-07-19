package app.sagen.consolecomputer.v2;

import java.util.Scanner;

public class ConsoleComputer {

    public static final int MEM_DIGIT_BUFFER      = 0xFFFF; // Location to r/w numbers
    public static final int MEM_LETTER_BUFFER     = 0xFFFE; // Location to r/w letters

    Scanner scanner = new Scanner(System.in);

    int pc = 0;
    int[] reg = new int[16];
    int[] mem = new int[0x10000]; // Address 0xFFFF is reserved for I/O

    // register instruction
    // |  op  |  d  |  s  |  t  |
    //    00     0     00    00
    // memory instruction
    // |  op  |  d  |   addr   |
    //    00     0      0000

    public void run() {
        while(true) {
            long inst    = mem[pc++];
            int op      = (int)(inst >> 20) & 0xF;     // 20 - 23
            int d       = (int)(inst >> 16) & 0xF;     // 16 - 19
            int s       = (int)(inst >> 8)  & 0xF;     //  8 - 15
            int t       = (int)inst         & 0xF;     //  0 -  7
            int addr    = (int) inst        & 0xFFFF;  //  0 - 15

            // debug
//            System.out.println();
//            System.out.print("OP: " + op + ", ");
//            System.out.print("Dest: " + Integer.toHexString(d).toUpperCase() + ", ");
//            System.out.print("S: " + Integer.toHexString(s).toUpperCase() + ", ");
//            System.out.print("T: " + Integer.toHexString(t).toUpperCase() + ", ");
//            System.out.println("Addr: " + Integer.toHexString(addr).toUpperCase());
//            System.out.println("Registers:");
//            for(int i = 0; i <= 0xF; i++) {
//                System.out.print("    R[" + i + "]=0x" + Integer.toHexString(reg[i]).toUpperCase());
//            }
//            System.out.println();

            // read I/O if requested
            if((addr == MEM_DIGIT_BUFFER && op == 8) ||
                    (reg[t] == MEM_DIGIT_BUFFER && op == 10)) {
                System.out.print("Input a hex number: ");
                mem[MEM_DIGIT_BUFFER] = scanner.nextInt(16) & 0xFFFF; // read input and ignore overflowing bits
            }

            if((addr == MEM_LETTER_BUFFER && op == 8) ||
                    (reg[t] == MEM_LETTER_BUFFER && op == 10)) {
                System.out.print("Input a letter: ");
                mem[MEM_LETTER_BUFFER] = scanner.next(".").charAt(0); // read input
            }

            switch(op) {
                case  0: return;                                        // stop
                case  1: reg[d] = reg[s] +  reg[t];           break;    // add
                case  2: reg[d] = reg[s] -  reg[t];           break;    // subtract
                case  3: reg[d] = reg[s] &  reg[t];           break;    // bitwise and
                case  4: reg[d] = reg[s] ^  reg[t];           break;    // bitwise xor
                case  5: reg[d] = reg[s] << reg[t];           break;    // shift left
                case  6: reg[d] = (short) reg[s] >> reg[t];   break;    // shift right
                case  7: reg[d] = addr;                       break;    // load address
                case  8: reg[d] = mem[addr];                  break;    // load
                case  9: mem[addr] = reg[d];                  break;    // store
                case 10: reg[d] = mem[reg[t]];                break;    // load indirect
                case 11: mem[reg[t]] = reg[d];                break;    // store indirect
                case 12: if ((short) reg[d] == 0) pc = addr;  break;    // branch if zero
                case 13: if ((short) reg[d] >  0) pc = addr;  break;    // branch if positive
                case 14: pc = reg[d];                         break;    // jump indirect
                case 15: reg[d] = pc; pc = addr;              break;    // jump and link
            }

            // write I/O if requested
            if((addr == MEM_DIGIT_BUFFER && op == 9) ||
                    (reg[t] == MEM_DIGIT_BUFFER && op == 11)) {
                System.out.println("Output: " + Long.toHexString(mem[MEM_DIGIT_BUFFER]).toUpperCase());
            }

            if((addr == MEM_LETTER_BUFFER && op == 9) ||
                    (reg[t] == MEM_LETTER_BUFFER && op == 11)) {
                System.out.print((char)(mem[MEM_LETTER_BUFFER] & 0xFFFF));
            }

            reg[0] = 0;                 // ensure is always 0

            // cut overflow
            reg[d] = reg[d] & 0xFFFF;   // 16 bit
            pc = pc & 0xFFFF;           // 16 bit
        }
    }

    public static void main(String[] args) {
        ConsoleComputer consoleComputer = new ConsoleComputer();

        // main function just jumps to print function
        consoleComputer.mem[0x0000] = 0x71A000; // store address to start of string
        consoleComputer.mem[0x0001] = 0xFF1000; // start by jumping to function to print
        consoleComputer.mem[0x0002] = 0x71B000; // store address to start of string
        consoleComputer.mem[0x0003] = 0xFF1000; // start by jumping to function to print
        consoleComputer.mem[0x0004] = 0x000000; // EOF

        // print function -> should return to last position
        consoleComputer.mem[0x1000] = 0xA20001; // Load character to R[2]
        // loop
        consoleComputer.mem[0x1001] = 0x92FFFE; // Print character R[2]
        consoleComputer.mem[0x1002] = 0x740001; // Load 1 to R[4]
        consoleComputer.mem[0x1003] = 0x110104; // R[1] = R[1] + R[4]
        consoleComputer.mem[0x1004] = 0xA20001; // Load character to R[2]
        consoleComputer.mem[0x1005] = 0xC2100F; // jump to return call
        consoleComputer.mem[0x1006] = 0xFA1001; // jump to 1001 unconditional and store address in R[A]
        // end loop
        consoleComputer.mem[0x100F] = 0xEF0000; // exit function

        // save 'Alex' to address 0xA000
        consoleComputer.mem[0xA000] = 0x41; // A
        consoleComputer.mem[0xA001] = 0x6C; // l
        consoleComputer.mem[0xA002] = 0x65; // e
        consoleComputer.mem[0xA003] = 0x78; // x
        consoleComputer.mem[0xA004] = 0x0A; // \n
        consoleComputer.mem[0xA005] = 0x00; // null-termination

        // save 'Hello, World!' to address 0xB000
        consoleComputer.mem[0xB000] = 'H';
        consoleComputer.mem[0xB001] = 'e';
        consoleComputer.mem[0xB002] = 'l';
        consoleComputer.mem[0xB003] = 'l';
        consoleComputer.mem[0xB004] = 'o';
        consoleComputer.mem[0xB005] = ',';
        consoleComputer.mem[0xB006] = 'W';
        consoleComputer.mem[0xB007] = 'o';
        consoleComputer.mem[0xB008] = 'r';
        consoleComputer.mem[0xB009] = 'l';
        consoleComputer.mem[0xB00A] = 'd';
        consoleComputer.mem[0xB00B] = '!';
        consoleComputer.mem[0xB00C] = '\n';
        consoleComputer.mem[0xB00D] = 0x000000; // null-termination

        consoleComputer.run();
    }
}
