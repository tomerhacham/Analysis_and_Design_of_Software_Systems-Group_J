package DataAccessLayer.DTO;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable(tableName = "DestFile")
public class DestFile_DTO {

    @DatabaseField(columnName = "transportID", foreign = true, foreignColumnName = "transportID", uniqueCombo = true, canBeNull = false)
    private Transport_DTO transportID;

    @DatabaseField(columnName = "productFileID",  foreign = true, foreignColumnName = "fileID" , uniqueCombo = true, canBeNull = false)
    private ProductFile_DTO productFileID;

    @DatabaseField(columnName = "siteID", foreign = true, foreignColumnName = "siteID", uniqueCombo = true, canBeNull = false)
    private Site_DTO siteID;

    public DestFile_DTO(Transport_DTO transportID, ProductFile_DTO productFileID, Site_DTO siteID){
        this.transportID=transportID;
        this.productFileID=productFileID;
        this.siteID=siteID;
    }

    public DestFile_DTO(){}

    public ProductFile_DTO getProductFileID() {
        return productFileID;
    }

    public Site_DTO getSiteID() {
        return siteID;
    }

    public Transport_DTO getTransportID() {
        return transportID;
    }

    public void setTransportID(Transport_DTO transportID) {
        this.transportID = transportID;
    }

    public void setSiteID(Site_DTO siteID) {
        this.siteID = siteID;
    }

    public void setProductFileID(ProductFile_DTO productFileID) {
        this.productFileID = productFileID;
    }
}
