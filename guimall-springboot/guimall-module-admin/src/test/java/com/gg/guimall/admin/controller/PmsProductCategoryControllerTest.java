package com.gg.guimall.admin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gg.guimall.admin.model.vo.pms.*;
import com.gg.guimall.admin.service.PmsProductCategoryService;
import com.gg.guimall.common.utils.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PmsProductCategoryControllerTest {

    @Mock
    private PmsProductCategoryService categoryService;

    @InjectMocks
    private PmsProductCategoryController categoryController;

    private PmsProductCategoryCreateReqVO createReqVO;
    private PmsProductCategoryUpdateReqVO updateReqVO;
    private FindPmsProductCategoryPageReqVO pageReqVO;

    @BeforeEach
    void setUp() {
        createReqVO = new PmsProductCategoryCreateReqVO();
        createReqVO.setParentId(0L);
        createReqVO.setName("Test Category");
        createReqVO.setLevel(0);
        createReqVO.setSort(1);

        updateReqVO = new PmsProductCategoryUpdateReqVO();
        updateReqVO.setId(1L);
        updateReqVO.setName("Updated Category");
        updateReqVO.setSort(2);

        pageReqVO = new FindPmsProductCategoryPageReqVO();
        pageReqVO.setPageNum(1);
        pageReqVO.setPageSize(10);
    }

    @Test
    void testAddCategory_Success() {
        doNothing().when(categoryService).addCategory(createReqVO);

        Response response = categoryController.addCategory(createReqVO);

        assertNotNull(response);
        assertTrue(response.isSuccess());
        verify(categoryService, times(1)).addCategory(createReqVO);
    }

    @Test
    void testAddCategory_ServiceThrowsException() {
        doThrow(new RuntimeException("Database error")).when(categoryService).addCategory(createReqVO);

        Response response = categoryController.addCategory(createReqVO);

        assertNotNull(response);
        assertFalse(response.isSuccess());
        verify(categoryService, times(1)).addCategory(createReqVO);
    }

    @Test
    void testUpdateCategory_Success() {
        doNothing().when(categoryService).updateCategory(updateReqVO);

        Response response = categoryController.updateCategory(updateReqVO);

        assertNotNull(response);
        assertTrue(response.isSuccess());
        verify(categoryService, times(1)).updateCategory(updateReqVO);
    }

    @Test
    void testUpdateCategory_ServiceThrowsException() {
        doThrow(new RuntimeException("Database error")).when(categoryService).updateCategory(updateReqVO);

        Response response = categoryController.updateCategory(updateReqVO);

        assertNotNull(response);
        assertFalse(response.isSuccess());
        verify(categoryService, times(1)).updateCategory(updateReqVO);
    }

    @Test
    void testDeleteCategory_Success() {
        Long id = 1L;
        doNothing().when(categoryService).deleteCategory(id);

        Response response = categoryController.deleteCategory(id);

        assertNotNull(response);
        assertTrue(response.isSuccess());
        verify(categoryService, times(1)).deleteCategory(id);
    }

    @Test
    void testDeleteCategory_ServiceThrowsException() {
        Long id = 1L;
        doThrow(new RuntimeException("Database error")).when(categoryService).deleteCategory(id);

        Response response = categoryController.deleteCategory(id);

        assertNotNull(response);
        assertFalse(response.isSuccess());
        verify(categoryService, times(1)).deleteCategory(id);
    }

    @Test
    void testFindCategoryPage_Success() {
        Page<FindPmsProductCategoryPageRspVO> expectedPage = new Page<>();
        expectedPage.setTotal(2);
        expectedPage.setCurrent(1);
        expectedPage.setSize(10);
        expectedPage.setRecords(Arrays.asList(new FindPmsProductCategoryPageRspVO(), new FindPmsProductCategoryPageRspVO()));
        when(categoryService.findCategoryPage(pageReqVO)).thenReturn(expectedPage);

        Page<FindPmsProductCategoryPageRspVO> response = categoryController.findCategoryPage(pageReqVO);

        assertNotNull(response);
        assertEquals(2, response.getTotal());
        verify(categoryService, times(1)).findCategoryPage(pageReqVO);
    }

    @Test
    void testFindCategoryPage_EmptyResult() {
        Page<FindPmsProductCategoryPageRspVO> expectedPage = new Page<>();
        expectedPage.setTotal(0);
        expectedPage.setCurrent(1);
        expectedPage.setSize(10);
        expectedPage.setRecords(Arrays.asList());
        when(categoryService.findCategoryPage(pageReqVO)).thenReturn(expectedPage);

        Page<FindPmsProductCategoryPageRspVO> response = categoryController.findCategoryPage(pageReqVO);

        assertNotNull(response);
        assertEquals(0, response.getTotal());
        assertTrue(response.getRecords().isEmpty());
        verify(categoryService, times(1)).findCategoryPage(pageReqVO);
    }

    @Test
    void testFindCategoryTree_Success() {
        FindPmsProductCategoryTreeRspVO root1 = new FindPmsProductCategoryTreeRspVO();
        root1.setId(1L);
        root1.setName("Root Category 1");

        FindPmsProductCategoryTreeRspVO child1 = new FindPmsProductCategoryTreeRspVO();
        child1.setId(2L);
        child1.setParentId(1L);
        child1.setName("Child Category 1");
        root1.setChildren(Arrays.asList(child1));

        FindPmsProductCategoryTreeRspVO root2 = new FindPmsProductCategoryTreeRspVO();
        root2.setId(3L);
        root2.setName("Root Category 2");

        List<FindPmsProductCategoryTreeRspVO> expectedTree = Arrays.asList(root1, root2);
        when(categoryService.findCategoryTree()).thenReturn(expectedTree);

        List<FindPmsProductCategoryTreeRspVO> response = categoryController.findCategoryTree();

        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals("Root Category 1", response.get(0).getName());
        assertNotNull(response.get(0).getChildren());
        assertEquals(1, response.get(0).getChildren().size());
        verify(categoryService, times(1)).findCategoryTree();
    }

    @Test
    void testFindCategoryTree_EmptyTree() {
        when(categoryService.findCategoryTree()).thenReturn(Arrays.asList());

        List<FindPmsProductCategoryTreeRspVO> response = categoryController.findCategoryTree();

        assertNotNull(response);
        assertTrue(response.isEmpty());
        verify(categoryService, times(1)).findCategoryTree();
    }

    @Test
    void testFindCategoryTree_SingleLevel() {
        FindPmsProductCategoryTreeRspVO root = new FindPmsProductCategoryTreeRspVO();
        root.setId(1L);
        root.setName("Single Category");
        root.setChildren(Arrays.asList());

        when(categoryService.findCategoryTree()).thenReturn(Arrays.asList(root));

        List<FindPmsProductCategoryTreeRspVO> response = categoryController.findCategoryTree();

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("Single Category", response.get(0).getName());
        assertTrue(response.get(0).getChildren().isEmpty());
        verify(categoryService, times(1)).findCategoryTree();
    }
}