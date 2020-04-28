package TestTransportModule;

import BusinessLayer.Transport.ProductsController;
import InterfaceLayer.Transport.FacadeController;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class ProductsControllerTest {
    ProductsController productsController;
    FacadeController facadeController;
    private int fileID;

    public ProductsControllerTest(){
        productsController = ProductsController.getInstance();
        facadeController = FacadeController.getInstance();
        createProducts();
    }

    private void createProducts()
    {
        fileID = facadeController.createProductsFile();
        facadeController.createProduct("Milk",3,0,3);
        facadeController.createProduct("Sugar",1,0,40);
        facadeController.createProduct("Salt",1,0,5);
        facadeController.createProduct("Rice",5,0,10);
    }

    @Test
    public void CheckWeights()
    {
        int weight = 3*3+1*40+1*5+5*10;
        assertEquals(weight, productsController.getFileWeight(0));

        String[] products_ID = new String[2];
        products_ID[0]="1";
        products_ID[1]="3";
        productsController.removeProducts(products_ID,0);

        //the previous weight
        assertNotEquals(weight, productsController.getFileWeight(0));

        //the new weight
        weight =weight- (1*40+5*10);
        assertEquals(weight, productsController.getFileWeight(0));
    }

}
