package com.gg.guimall.admin.service.impl;

import com.gg.guimall.admin.model.vo.pms.PmsProductAttributeCreateReqVO;
import com.gg.guimall.admin.model.vo.pms.PmsProductAttributeUpdateReqVO;
import com.gg.guimall.common.domain.dos.PmsProductAttributeCategoryDO;
import com.gg.guimall.common.domain.dos.PmsProductAttributeDO;
import com.gg.guimall.common.domain.mapper.PmsProductAttributeCategoryMapper;
import com.gg.guimall.common.domain.mapper.PmsProductAttributeMapper;
import com.gg.guimall.common.domain.mapper.PmsProductAttributeValueMapper;
import com.gg.guimall.common.enums.ResponseCodeEnum;
import com.gg.guimall.common.exception.BizException;
import com.gg.guimall.common.utils.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

/**
 * @author: wly
 * @url: www.gg.com
 * @date: 2026/3/13
 * @description: 商品属性 Service 单元测试
 **/
@ExtendWith(MockitoExtension.class)
public class PmsProductAttributeServiceImplTest {

    @Mock
    private PmsProductAttributeMapper attributeMapper;

    @Mock
    private PmsProductAttributeCategoryMapper attributeCategoryMapper;

    @Mock
    private PmsProductAttributeValueMapper attributeValueMapper;

    @InjectMocks
    private PmsProductAttributeServiceImpl attributeService;

    @Test
    void testCreateAttribute_Success() {
        PmsProductAttributeCreateReqVO reqVO = new PmsProductAttributeCreateReqVO();
        reqVO.setProductAttributeCategoryId(1L);
        reqVO.setName("颜色");
        reqVO.setType(0);
        reqVO.setInputType(1);
        reqVO.setInputList("红色,蓝色,绿色");

        PmsProductAttributeCategoryDO categoryDO = new PmsProductAttributeCategoryDO();
        categoryDO.setId(1L);
        categoryDO.setAttributeCount(0);
        categoryDO.setParamCount(0);

        when(attributeCategoryMapper.selectById(1L)).thenReturn(categoryDO);
        when(attributeMapper.selectOne(any())).thenReturn(null);

        Response response = attributeService.createAttribute(reqVO);

        Assertions.assertTrue(response.isSuccess());
        verify(attributeMapper, times(1)).insert(argThat(attribute ->
                attribute.getName().equals("颜色") &&
                attribute.getType() == 0
        ));
        verify(attributeCategoryMapper, times(2)).selectById(1L);
        verify(attributeCategoryMapper, times(1)).updateById(any());
    }

    @Test
    void testCreateAttribute_CategoryNotFound() {
        PmsProductAttributeCreateReqVO reqVO = new PmsProductAttributeCreateReqVO();
        reqVO.setProductAttributeCategoryId(999L);
        reqVO.setName("颜色");
        reqVO.setType(0);

        when(attributeCategoryMapper.selectById(999L)).thenReturn(null);

        BizException exception = Assertions.assertThrows(BizException.class, () -> {
            attributeService.createAttribute(reqVO);
        });

        Assertions.assertEquals(ResponseCodeEnum.ATTRIBUTE_CATEGORY_NOT_FOUND.getErrorCode(), exception.getErrorCode());
    }

    @Test
    void testCreateAttribute_NameExists() {
        PmsProductAttributeCreateReqVO reqVO = new PmsProductAttributeCreateReqVO();
        reqVO.setProductAttributeCategoryId(1L);
        reqVO.setName("已存在的属性");
        reqVO.setType(0);

        PmsProductAttributeCategoryDO categoryDO = new PmsProductAttributeCategoryDO();
        categoryDO.setId(1L);

        PmsProductAttributeDO existAttribute = new PmsProductAttributeDO();
        existAttribute.setId(1L);
        existAttribute.setName("已存在的属性");

        when(attributeCategoryMapper.selectById(1L)).thenReturn(categoryDO);
        when(attributeMapper.selectOne(any())).thenReturn(existAttribute);

        BizException exception = Assertions.assertThrows(BizException.class, () -> {
            attributeService.createAttribute(reqVO);
        });

        Assertions.assertEquals(ResponseCodeEnum.ATTRIBUTE_NAME_EXISTS.getErrorCode(), exception.getErrorCode());
    }

    @Test
    void testUpdateAttribute_Success() {
        PmsProductAttributeUpdateReqVO reqVO = new PmsProductAttributeUpdateReqVO();
        reqVO.setId(1L);
        reqVO.setProductAttributeCategoryId(1L);
        reqVO.setName("更新后的属性");
        reqVO.setType(0);

        PmsProductAttributeDO existAttribute = new PmsProductAttributeDO();
        existAttribute.setId(1L);
        existAttribute.setProductAttributeCategoryId(1L);
        existAttribute.setType(0);

        PmsProductAttributeCategoryDO categoryDO = new PmsProductAttributeCategoryDO();
        categoryDO.setId(1L);

        when(attributeMapper.selectById(1L)).thenReturn(existAttribute);
        when(attributeCategoryMapper.selectById(1L)).thenReturn(categoryDO);
        when(attributeMapper.selectOne(any())).thenReturn(null);

        Response response = attributeService.updateAttribute(reqVO);

        Assertions.assertTrue(response.isSuccess());
        verify(attributeMapper, times(1)).updateById(any(PmsProductAttributeDO.class));
    }

    @Test
    void testDeleteAttribute_Success() {
        Long id = 1L;
        PmsProductAttributeDO attributeDO = new PmsProductAttributeDO();
        attributeDO.setId(1L);
        attributeDO.setProductAttributeCategoryId(1L);
        attributeDO.setType(0);

        PmsProductAttributeCategoryDO categoryDO = new PmsProductAttributeCategoryDO();
        categoryDO.setId(1L);
        categoryDO.setAttributeCount(5);

        when(attributeMapper.selectById(id)).thenReturn(attributeDO);
        when(attributeValueMapper.selectCount(any())).thenReturn(0L);
        when(attributeCategoryMapper.selectById(1L)).thenReturn(categoryDO);

        Response response = attributeService.deleteAttribute(id);

        Assertions.assertTrue(response.isSuccess());
        verify(attributeMapper, times(1)).deleteById(id);
        verify(attributeCategoryMapper, times(1)).updateById(argThat(category ->
                category.getAttributeCount() == 4
        ));
    }

    @Test
    void testDeleteAttribute_InUse() {
        Long id = 1L;
        PmsProductAttributeDO attributeDO = new PmsProductAttributeDO();
        attributeDO.setId(1L);

        when(attributeMapper.selectById(id)).thenReturn(attributeDO);
        when(attributeValueMapper.selectCount(any())).thenReturn(3L);

        BizException exception = Assertions.assertThrows(BizException.class, () -> {
            attributeService.deleteAttribute(id);
        });

        Assertions.assertEquals(ResponseCodeEnum.ATTRIBUTE_IN_USE.getErrorCode(), exception.getErrorCode());
    }
}
