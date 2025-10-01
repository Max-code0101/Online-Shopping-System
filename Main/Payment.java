//getter and setter,constructor,declaration

public class Payment{

    private int bankType;
    private int cardNo;
    private int pin;
    private double transferAmount;
    private int receiptNo;
     

    public static final int MAYBANK= 1;
    public static final int PUBLIC_BANK= 2;
    public static final int CIMB_BANK = 3;
    public static final int AMBANK = 4;
    public static final int AFFIN_BANK= 5;
 
    public Payment(int bankType , int cardNo , int pin, double transferAmount , int receiptNo){
        this.bankType = bankType;
        this.cardNo = cardNo;
        this.pin = pin;
        this.transferAmount = transferAmount;
        this.receiptNo = receiptNo;

    }
    public Payment(){
        this.bankType = 0;
        this.cardNo = 0;
        this.pin = 0;
        this.transferAmount = 0;
        this.receiptNo = 0;
    }

    public int getBankType(){
        return bankType;
    }

    public void setBankType(int bankType){
        this.bankType = bankType;
    }
    public int getCardNo(){
        return cardNo;
    }

    public void setCardNo(int cardNo){
        this.cardNo = cardNo;
    }

    public int getPin(){
        return pin;
    }

    public int getReceiptNo(){
        return receiptNo;
    }

    public void setPin(int pin){
        this.pin = pin;
    }

    public double getTransferAmount(){
        return transferAmount;
    }
    
    public void setTransferAmount(double transferAmount){
        this.transferAmount = transferAmount;
    }

    public void setReceiptNo(int receiptNo){
        this.receiptNo = receiptNo;
    }

    @Override
      public String toString(){
        return "bank type: " +  bankType + "\n" + 
                "card No: " +  cardNo + "\n" + 
                "trnasfer amount: " +  transferAmount + "\n" + 
                "receipt No: " + receiptNo + "\n";
      }
}

