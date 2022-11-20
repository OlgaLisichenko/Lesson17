public class Document {

    private String phone = "No phone number";
    private String email = "No e-mail";
    private String docNum = "No document number";

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDocNum(String docNum) {
        this.docNum = docNum;
    }

    @Override
    public String toString() {
        return " Document: phone = " + phone +
               ", e-mail = " + email +
               ", document number = " + docNum;
    }
}
