package app.sagen.consolecomputer.v1;

import java.util.Scanner;

public class ConsoleComputer {

    public static final int HALT = 0x00;   // Exit program
    public static final int STORE = 0x01;  // Store AX value to Memory
    public static final int LOAD = 0x02;   // Load from Memory to AX
    public static final int ADDI = 0x03;   // Add constant to AX
    public static final int SUBI = 0x04;   // Subtract constant from AX
    public static final int ADD = 0x05;    // Add memory value to AX
    public static final int SUB = 0x06;    // Subtract memory value from AX
    public static final int MUL = 0x07;    // Multiply memory value with AX
    public static final int DIV = 0x08;    // Divide AX by memory value
    public static final int IN = 0x09;     // Read input to AX
    public static final int PRTNUM = 0x0A; // Print AX to Output
    public static final int JGZ = 0x0B;    // Jump if AX greater than zero
    public static final int JZERO = 0x0C;  // Jump if AX equals zero
    public static final int PRTCHR = 0x0D; // Print character
    public static final int LOADPTR = 0x0E;// Load dynamic from Memory to AX
    public static final int NOOP = 0xFE;   // Do nothing
    public static final int ILLEGAL = 0xFF;

    Scanner scanner = new Scanner(System.in);

    int instruction;

    // registers
    int pc = 0;
    int ax = 0;

    // memory
    int[] memory;

    public ConsoleComputer(int[] memory) {
        this.memory = memory;
    }

    public boolean executeStep() {
        int instruction = executeInstruction();
        if(instruction == HALT) {
            System.out.println("Program has requested normal program termination!");
            return false;
        }
        if(instruction == ILLEGAL) {
            System.out.println("An illegal instruction was found and the program was terminated!");
            return false;
        }
        return true;
    }

    public int fetchInstruction() {
        return memory[pc++];
    }

    public int fetchAddress() {
        return memory[pc++];
    }

    public int fetchConstant() {
        return memory[pc++];
    }

    public int executeInstruction() {
        int address, constant, memVal;

        instruction = fetchInstruction();

        switch(instruction) {
            case STORE:
                address = fetchAddress();
                memory[address] = ax;
                break;
            case LOAD:
                address = fetchAddress();
                ax = memory[address];
                break;
            case LOADPTR:
                address = fetchAddress();
                memVal = memory[address];
                ax = memory[memVal];
                break;
            case ADDI:
                constant = fetchConstant();
                ax += constant;
                break;
            case SUBI:
                constant = fetchConstant();
                ax -= constant;
                break;
            case ADD:
                address = fetchAddress();
                memVal = memory[address];
                ax += memVal;
                break;
            case SUB:
                address = fetchAddress();
                memVal = memory[address];
                ax -= memVal;
                break;
            case MUL:
                address = fetchAddress();
                memVal = memory[address];
                ax *= memVal;
                break;
            case DIV:
                address = fetchAddress();
                memVal = memory[address];
                ax /= memVal;
                break;
            case IN:
                System.out.print("Input a value: ");
                int input = scanner.nextInt();
                ax = input;
                break;
            case PRTNUM:
                System.out.print(ax);
                break;
            case JGZ:
                address = fetchAddress();
                if(ax > 0) pc = address;
                break;
            case JZERO:
                address = fetchAddress();
                if(ax == 0) pc = address;
                break;
            case PRTCHR:
                System.out.print((char)ax);
            case HALT:
            case NOOP:
                break;
            default:
                instruction = ILLEGAL;
                System.out.println("Illegal instruction at location " + pc);
                break;
        }

        return instruction;
    }

}
