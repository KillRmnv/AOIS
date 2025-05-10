package by.romanoff.aois;
public class Main {
    public static void main(String[] args) {
        UI ui = new UI();
        ui.LogicExpressionProgramm();
    }
}
//!(A->!C)&B|(A~D)
//(E&Q&T)|(B&C&!D&!E)|(!A&D&Q&T)|(!A&!B&!E&!T)|(!A&!B&!E&!Q)|(A&B&!D&!E&Q&T)|(A&!B&C&E&!T)|(A&!B&!C&E&!T)|(A&!B&!C&E&!Q)|(A&!B&C&E&!Q)