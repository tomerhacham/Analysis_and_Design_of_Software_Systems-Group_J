package data_access_layer.DTO;


import bussines_layer.SupplierCard;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

enum supplierType {byOrder , periodic , selfDelivery;}

@DatabaseTable(tableName = "Supplier")
public class SupplierDTO {
    //fields:
    @DatabaseField(id=true, columnName = "supplier_id")
    Integer supplier_id;
    @DatabaseField(columnName = "supplier_name")
    String supplier_name;
    @DatabaseField(columnName = "address")
    String address;
    @DatabaseField(columnName = "email")
    String email;
    @DatabaseField(columnName = "phone_number")
    String phone_number;
    @DatabaseField(columnName = "bank_account_number")
    String bank_account_number;
    @DatabaseField(columnName = "payment_kind")
    String payment_kind;
    @DatabaseField(columnName = "type",dataType = DataType.ENUM_TO_STRING)
    supplierType type;
    @ForeignCollectionField(eager=false)
    ForeignCollection<contact_of_supplierDTO> contact_list;

    //Constructor
    public SupplierDTO(Integer supplier_id, String supplier_name, String address,
                       String email, String phone_number, String bank_account_number,
                       String payment_kind,String type) {
        this.supplier_id = supplier_id;
        this.supplier_name = supplier_name;
        this.address = address;
        this.email = email;
        this.phone_number = phone_number;
        this.bank_account_number = bank_account_number;
        this.payment_kind = payment_kind;
        this.type=convertEnumTString(type);
    }
    public SupplierDTO(SupplierCard supplierCard){
        this.supplier_id = supplierCard.getId();
        this.supplier_name = supplierCard.getSupplierName();
        this.address = supplierCard.getAddress();
        this.email = supplierCard.getEmail();
        this.phone_number = supplierCard.getPhoneNumber();
        this.bank_account_number = supplierCard.getBankAccountNum();
        this.payment_kind = supplierCard.getPayment();
        this.type=convertEnumTString(supplierCard.getType().name());
    }
    public SupplierDTO() {
    }
    //region Methods

    public Integer getSupplier_id() {
        return supplier_id;
    }

    public String getSupplier_name() {
        return supplier_name;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public String getBank_account_number() {
        return bank_account_number;
    }

    public String getPayment_kind() {
        return payment_kind;
    }

    public supplierType getType() {
        return type;
    }

    public ForeignCollection<contact_of_supplierDTO> getContact_list() {
        return contact_list;
    }

    public supplierType convertEnumTString(String type){
        if(type.equals("byOrder")){return supplierType.byOrder;}
        else if(type.equals("periodic")){return supplierType.periodic;}
        else{return supplierType.selfDelivery;}
    }

    @Override
    public String toString() {
        return "SupplierDTO{" +
                "supplier_id=" + supplier_id +
                ", supplier_name='" + supplier_name + '\'' +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                ", phone_number='" + phone_number + '\'' +
                ", bank_account_number='" + bank_account_number + '\'' +
                ", payment_kind='" + payment_kind + '\'' +
                ", type=" + type +
                '}';
    }
    //endregion
}
