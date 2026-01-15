package com.example.HCOData.repository;

import com.example.HCOData.model.Image;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Repository
public interface ImageJPARepository extends PagingAndSortingRepository<Image, Long> {

    @Query("SELECT I.name from Image I where I.uid = :uid")
    String getNameImage(@Param("uid") String uid);

    List<Image> findByStatusModerationIn(List<String> statusModerationList);

    List<Image> findByStatusModeration(String statusModeration);
    Image findByUid(String uid);

    @Query("SELECT I from Image I inner join I.barecodes b inner join I.operations o where o.uid = :idOperation and b.data = :EAN and NOT I.uid = :uid")
    Set<Image> findImageByEan(@Param("EAN") String EAN, @Param("uid") String uid, @Param("idOperation") String idOperation);

    @Query("SELECT I " +
            "FROM Image I " +
            "INNER JOIN I.operations o " +
            "WHERE o.uid = :operationUid " +
            "AND I.buyHour = :buyHour " +
            "AND I.sign = :storeName " +
            "AND I.city = :city " +
            "AND I.buyDate = :buyDate " +
            "AND NOT I.uid = :imageUid")
    List<Image> findImagesWithSameShopSameMoment(
            @Param("storeName") String storeName,
            @Param("city") String city,
            @Param("buyDate") String buyDate,
            @Param("buyHour") String buyHour,
            @Param("imageUid") String imageUid,
            @Param("operationUid") String operationUid
    );

    @Query("SELECT I " +
            "FROM Image I " +
            "INNER JOIN I.operations o " +
            "WHERE o.uid = :idOperation " +
            "AND I.articleCount = :articleCount " +
            "AND I.imageMD5 = :imageMD5 " +
            "AND I.buyHour = :buyHour " +
            "AND I.buyDate = :buyDate " +
            "AND I.city = :city " +
            "AND I.address = :address " +
            "AND I.total = :total " +
            "AND I.totalMax = :totalMax " +
            "AND I.sign = :sign " +
            "AND NOT I.uid = :uid")
    List<Image> findImageWithSameData(
            @Param("buyDate") String buyDate,
            @Param("city") String city,
            @Param("buyHour") String buyHour,
            @Param("address") String address,
            @Param("total") BigDecimal total,
            @Param("totalMax") BigDecimal totalMax,
            @Param("articleCount") Long articleCount,
            @Param("sign") String sign,
            @Param("uid") String uid,
            @Param("idOperation") String idOperation,
            @Param("imageMD5") String imageMD5
    );


    @Query(value = "select * from get_image_uid2(:idOperation,:imageUid,:productCount)", nativeQuery = true)
    Set<String> findImageWithSameProducts(@Param("idOperation") String idOperation, @Param("imageUid") String imageUid, @Param("productCount") int productCount);


}
