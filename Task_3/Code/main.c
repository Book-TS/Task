#include <msp430.h>

#define TX1_Pin BIT0       // Uart1 TX P2.0
#define RX1_Pin BIT1       // Uart1 RX P2.1
#define TX2_Pin BIT4       // Uart2 TX P3.4
#define RX2_Pin BIT5       // Uart2 RX P3.5

#define LED_R   BIT0       // LED RED P1.0
#define LED_G   BIT7       // LED GREEN P9.7

unsigned char RXData_1 = 0;
unsigned char TXData_1 = 1;
unsigned char RXData_2 = 0;
unsigned char TXData_2 = 1;

void io_Init(void);
void XT1_Init(void);
void uart1_Init(void);
void uart2_Init(void);
void sendChr_1(char data);
void sendChr_2(char data);
void SendStr_1(char* str);
void SendStr_2(char* str);

int main(void)
{
  WDTCTL = WDTPW | WDTHOLD;                 // stop watchdog

  io_Init();
  
  XT1_Init();

  uart1_Init();
  SendStr_1("start_Uart 1");
  
  
  uart2_Init();
  SendStr_2("start_Uart 2");
  
  while (1)
  {
    sendChr_1(TXData_1);
    sendChr_2(TXData_2);

    __bis_SR_register(LPM0_bits | GIE);     // Enter LPM0, interrupts enabled
  }
}

// Configure IO
void io_Init(void) {
  P1OUT &= ~LED_R;                           // Clear P1.0 output latch
  P9OUT &= ~LED_G;                           // Clear P9.7 output latch
  P1DIR |= LED_R;                            // LED on P1.0
  P9DIR |= LED_G;                            // LED on P9.7
  
  // Configure UART pins 
  P2SEL0 |= TX1_Pin | RX1_Pin;               // P2.0 TX - P2.1 RX                   
  P2SEL1 &= ~(RX1_Pin | RX1_Pin);
  
  P3SEL0 |= TX2_Pin | RX2_Pin;               // P3.4 TX - P3.5 RX                   
  P3SEL1 &= ~(RX2_Pin | RX2_Pin);
  
  PJSEL0 |= BIT4 | BIT5;                     // Configure XT1 pins
  
  PM5CTL0 &= ~LOCKLPM5;
}

// Configure XT1
void XT1_Init(void) {
  CSCTL0_H = CSKEY >> 8;                     // Unlock CS registers
  CSCTL1 = DCOFSEL_0;                        // Set DCO to 1MHz
  CSCTL2 = SELA__LFXTCLK | SELS__DCOCLK | SELM__DCOCLK;
  CSCTL3 = DIVA__1 | DIVS__1 | DIVM__1;      // Set all dividers
  CSCTL4 &= ~LFXTOFF;                        // Enable LFXT1
  do
  {
    CSCTL5 &= ~LFXTOFFG;                     // Clear XT1 fault flag
    SFRIFG1 &= ~OFIFG;
  } while (SFRIFG1&OFIFG);                   // Test oscillator fault flag
  CSCTL0_H = 0;                              // Lock CS registers
}

// Configure USCI_A0 for UART1
void uart1_Init(void) {
  UCA0CTL1 |= UCSWRST;
  UCA0CTL1 = UCSSEL__ACLK;                  // Set ACLK = 32768 as UCBRCLK
  UCA0BR0 = 3;                              // 9600 baudrate
  UCA0MCTLW |= 0x5300;                      // 32768/9600 - INT(32768/9600)=0.41
                                            // UCBRSx value = 0x53 (See UG)
  UCA0BR1 = 0;
  UCA0CTL1 &= ~UCSWRST;                     // release from reset
  UCA0IE |= UCRXIE;                         // Enable USCI_A0 RX interrupt
}

// Configure USCI_A1 for UART2
void uart2_Init(void) {
  UCA1CTL1 |= UCSWRST;
  UCA1CTL1 = UCSSEL__ACLK;                  // Set ACLK = 32768 as UCBRCLK
  UCA1BR0 = 3;                              // 9600 baudrate
  UCA1MCTLW |= 0x5300;                      // 32768/9600 - INT(32768/9600)=0.41
                                            // UCBRSx value = 0x53 (See UG)
  UCA1BR1 = 0;
  UCA1CTL1 &= ~UCSWRST;                     // release from reset
  UCA1IE |= UCRXIE;                         // Enable USCI_A0 RX interrupt
}

// Uart1 Send
void sendChr_1(char data) { 
  while(!(UCA0IFG & UCTXIFG));
    UCA0TXBUF = data;
}

// Uart2 Send
void sendChr_2(char data) { 
  while(!(UCA1IFG & UCTXIFG));
    UCA1TXBUF = data;
}

void SendStr_1(char* str){
  while(*str){
     sendChr_1(*str);
      str++;      
  }
  sendChr_1(0);
}

void SendStr_2(char* str){
  while(*str){
     sendChr_2(*str);
      str++; 
  }
  sendChr_2(0);
}

// Interrupt Uart1
#pragma vector = USCI_A0_VECTOR             
__interrupt void USCI_A0_ISR(void)
{
  UCA0IFG &= ~UCRXIFG;                          // Clear interrupt
  RXData_1 = UCA0RXBUF;                         // Clear buffer
  if(RXData_1 != TXData_1)                      // Check value
  {
    P1OUT |= LED_R;                             // If incorrect turn on P1.0
      while(1);                                 // Trap CPU
  }
  TXData_1++;                                  // Increment TX data
  __bic_SR_register_on_exit(CPUOFF);            // Exit LPM0 on reti
}

// Interrupt Uart2
#pragma vector = USCI_A1_VECTOR             
__interrupt void USCI_A1_ISR(void)
{
  UCA1IFG &= ~UCRXIFG;                      // Clear interrupt
  RXData_2 = UCA1RXBUF;                    // Clear buffer
  if(RXData_2 != TXData_2)                 // Check value
  {
    P9OUT |= LED_G;                         // If incorrect turn on P1.0
      while(1);                             // Trap CPU
  }
  TXData_2++;                               // Increment TX data
  __bic_SR_register_on_exit(CPUOFF);        // Exit LPM0 on reti
}

