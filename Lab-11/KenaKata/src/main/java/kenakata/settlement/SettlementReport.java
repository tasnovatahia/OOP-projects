package kenakata.settlement;
import java.util.LinkedHashMap;
import java.util.Map;
import kenakata.seller.Seller;
import java.util.Collections;
public final class SettlementReport {
    private final int day;
    private final Map<Seller,Long>sellerPayouts;
    private final long platformRevenue;

    public SettlementReport(int day,Map<Seller,Long>sellerPayouts,long platformRevenue){
        if(day<0){
            throw  new IllegalArgumentException("Settlement cannot be negative.");
        }

    if(sellerPayouts==null){
    throw new IllegalArgumentException("Seller payouts cannot be null.");
    }
    if(platformRevenue<0){
        throw new IllegalArgumentException("Platform revenue cannot be negative.");
    }
    this.day=day;
    this.sellerPayouts=Collections.unmodifiableMap(new LinkedHashMap<>(sellerPayouts));
    this.platformRevenue=platformRevenue;
    }

    public int getDay(){
        return day;
    }
    public Map<Seller,Long>getSellerPayouts(){
        return sellerPayouts;
    }

    public long getSellerPayout(Seller seller){
        if(seller==null){
            throw new IllegalArgumentException("Seller cannot be null");
        }
        return sellerPayouts.getOrDefault(seller,0L);
    }

    public long payoutFor(Seller seller){
        return  getSellerPayout(seller);
    }
    public long getPayOut(Seller seller){
        return  getSellerPayout(seller);
    }

    public long getPlatformRevenue(){
        return platformRevenue;
    }

   public long platformRevenue(){
        return platformRevenue;
   }

}
