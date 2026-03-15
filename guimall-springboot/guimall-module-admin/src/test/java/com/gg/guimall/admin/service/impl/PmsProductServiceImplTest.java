package com.gg.guimall.admin.service.impl;

import com.gg.guimall.admin.model.vo.pms.PmsProductCreateReqVO;
import com.gg.guimall.admin.model.vo.pms.PmsProductUpdateReqVO;
import com.gg.guimall.common.domain.dos.PmsProductDO;
import com.gg.guimall.common.domain.mapper.PmsProductMapper;
import com.gg.guimall.common.enums.ResponseCodeEnum;
import com.gg.guimall.common.exception.BizException;
import com.gg.guimall.common.utils.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PmsProductServiceImplTest {

    @Mock
    private PmsProductMapper pmsProductMapper;

    @InjectMocks
    private PmsProductServiceImpl pmsProductService;

    @Test
    void testCreateProduct_DefaultPublishStatus() {
        PmsProductCreateReqVO reqVO = new PmsProductCreateReqVO();
        reqVO.setProductCategoryId(1L);
        reqVO.setFarmerId(1L);
        reqVO.setName("Test Product");
        reqVO.setProductSn("SN123");
        reqVO.setPrice(new BigDecimal("100.00"));
        reqVO.setStock(10);
        // publishStatus is null by default in new PmsProductCreateReqVO()
        // But wait, I set a default value in the VO class!
        // Let's see if it's 0.

        Response response = pmsProductService.createProduct(reqVO);

        Assertions.assertEquals("200", response.getErrorCode());
        verify(pmsProductMapper, times(1)).insert(argThat(product -> 
            product.getPublishStatus() != null && product.getPublishStatus() == 0
        ));
    }

    @Test
    void testUpdateProduct_ProductNotFound() {
        PmsProductUpdateReqVO reqVO = new PmsProductUpdateReqVO();
        reqVO.setId(999L);
        reqVO.setProductCategoryId(1L);
        reqVO.setFarmerId(1L);
        reqVO.setName("Update Product");
        reqVO.setProductSn("SN456");
        reqVO.setPrice(new BigDecimal("200.00"));
        reqVO.setStock(20);

        when(pmsProductMapper.selectById(999L)).thenReturn(null);

        BizException exception = Assertions.assertThrows(BizException.class, () -> {
            pmsProductService.updateProduct(reqVO);
        });

        Assertions.assertEquals(ResponseCodeEnum.PRODUCT_NOT_FOUND.getErrorCode(), exception.getErrorCode());
    }

    @Test
    void testUpdateProduct_Success() {
        PmsProductUpdateReqVO reqVO = new PmsProductUpdateReqVO();
        reqVO.setId(1L);
        reqVO.setProductCategoryId(1L);
        reqVO.setFarmerId(1L);
        reqVO.setName("Update Product");
        reqVO.setProductSn("SN456");
        reqVO.setPrice(new BigDecimal("200.00"));
        reqVO.setStock(20);

        PmsProductDO existingProduct = new PmsProductDO();
        existingProduct.setId(1L);

        when(pmsProductMapper.selectById(1L)).thenReturn(existingProduct);

        Response response = pmsProductService.updateProduct(reqVO);

        Assertions.assertEquals("200", response.getErrorCode());
        verify(pmsProductMapper, times(1)).updateById(any(PmsProductDO.class));
    }

    @Test
    void testDeleteProduct_ProductNotFound() {
        Long id = 999L;
        when(pmsProductMapper.selectById(id)).thenReturn(null);

        BizException exception = Assertions.assertThrows(BizException.class, () -> {
            pmsProductService.deleteProduct(id);
        });

        Assertions.assertEquals(ResponseCodeEnum.PRODUCT_NOT_FOUND.getErrorCode(), exception.getErrorCode());
    }

    @Test
    void testDeleteProduct_Success() {
        Long id = 1L;
        PmsProductDO existingProduct = new PmsProductDO();
        existingProduct.setId(1L);

        when(pmsProductMapper.selectById(id)).thenReturn(existingProduct);

        Response response = pmsProductService.deleteProduct(id);

        Assertions.assertEquals("200", response.getErrorCode());
        verify(pmsProductMapper, times(1)).deleteById(id);
    }
}
