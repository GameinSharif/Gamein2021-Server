package ir.sharif.gamein2021.core.dao;

import ir.sharif.gamein2021.core.domain.entity.StorageProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StorageProductRepository extends JpaRepository<StorageProduct, Integer> {

}
