package Models;

public class Member {
    private int memberId;
    private String name;
    private String email;
    private String phone;

    // Constructor
    public Member(int memberId, String name, String email, String phone) {
        this.memberId = memberId;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    // Constructor without ID for new members
    public Member(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    // Getters and Setters
    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return String.format(
            "👤 Member Details:\n" +
            "--------------------------\n" +
            "🆔 ID      : %d\n" +
            "📛 Name    : %s\n" +
            "📧 Email   : %s\n" +
            "📞 Phone   : %s\n" +
            "--------------------------",
            memberId, name, email, phone
        );
    }
    
}
