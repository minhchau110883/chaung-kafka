package com.chaung.kafka.web.rest;

import com.chaung.kafka.ChaungKafkaApp;

import com.chaung.kafka.domain.Point;
import com.chaung.kafka.domain.Campaign;
import com.chaung.kafka.repository.PointRepository;
import com.chaung.kafka.service.PointService;
import com.chaung.kafka.service.dto.PointDTO;
import com.chaung.kafka.service.mapper.PointMapper;
import com.chaung.kafka.web.rest.errors.ExceptionTranslator;
import com.chaung.kafka.service.dto.PointCriteria;
import com.chaung.kafka.service.PointQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static com.chaung.kafka.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the PointResource REST controller.
 *
 * @see PointResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ChaungKafkaApp.class)
public class PointResourceIntTest {

    private static final Long DEFAULT_OUTLET_ID = 1L;
    private static final Long UPDATED_OUTLET_ID = 2L;

    private static final Long DEFAULT_TERMINAL_ID = 1L;
    private static final Long UPDATED_TERMINAL_ID = 2L;

    private static final Long DEFAULT_MERCHANT_ID = 1L;
    private static final Long UPDATED_MERCHANT_ID = 2L;

    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;

    private static final Long DEFAULT_CAMPAIGN_ID = 1L;
    private static final Long UPDATED_CAMPAIGN_ID = 2L;

    private static final Long DEFAULT_PAYMENT_TRANSACTION = 1L;
    private static final Long UPDATED_PAYMENT_TRANSACTION = 2L;

    private static final String DEFAULT_PAYMENT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_PAYMENT_TYPE = "BBBBBBBBBB";

    private static final Double DEFAULT_AMOUNT = 1D;
    private static final Double UPDATED_AMOUNT = 2D;

    private static final Integer DEFAULT_POINT = 1;
    private static final Integer UPDATED_POINT = 2;

    @Autowired
    private PointRepository pointRepository;

    @Autowired
    private PointMapper pointMapper;

    @Autowired
    private PointService pointService;

    @Autowired
    private PointQueryService pointQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPointMockMvc;

    private Point point;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PointResource pointResource = new PointResource(pointService, pointQueryService);
        this.restPointMockMvc = MockMvcBuilders.standaloneSetup(pointResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Point createEntity(EntityManager em) {
        Point point = new Point()
            .outletId(DEFAULT_OUTLET_ID)
            .terminalId(DEFAULT_TERMINAL_ID)
            .merchantId(DEFAULT_MERCHANT_ID)
            .userId(DEFAULT_USER_ID)
            .campaignId(DEFAULT_CAMPAIGN_ID)
            .paymentTransaction(DEFAULT_PAYMENT_TRANSACTION)
            .paymentType(DEFAULT_PAYMENT_TYPE)
            .amount(DEFAULT_AMOUNT)
            .point(DEFAULT_POINT);
        return point;
    }

    @Before
    public void initTest() {
        point = createEntity(em);
    }

    @Test
    @Transactional
    public void createPoint() throws Exception {
        int databaseSizeBeforeCreate = pointRepository.findAll().size();

        // Create the Point
        PointDTO pointDTO = pointMapper.toDto(point);
        restPointMockMvc.perform(post("/api/points")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pointDTO)))
            .andExpect(status().isCreated());

        // Validate the Point in the database
        List<Point> pointList = pointRepository.findAll();
        assertThat(pointList).hasSize(databaseSizeBeforeCreate + 1);
        Point testPoint = pointList.get(pointList.size() - 1);
        assertThat(testPoint.getOutletId()).isEqualTo(DEFAULT_OUTLET_ID);
        assertThat(testPoint.getTerminalId()).isEqualTo(DEFAULT_TERMINAL_ID);
        assertThat(testPoint.getMerchantId()).isEqualTo(DEFAULT_MERCHANT_ID);
        assertThat(testPoint.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testPoint.getCampaignId()).isEqualTo(DEFAULT_CAMPAIGN_ID);
        assertThat(testPoint.getPaymentTransaction()).isEqualTo(DEFAULT_PAYMENT_TRANSACTION);
        assertThat(testPoint.getPaymentType()).isEqualTo(DEFAULT_PAYMENT_TYPE);
        assertThat(testPoint.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testPoint.getPoint()).isEqualTo(DEFAULT_POINT);
    }

    @Test
    @Transactional
    public void createPointWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = pointRepository.findAll().size();

        // Create the Point with an existing ID
        point.setId(1L);
        PointDTO pointDTO = pointMapper.toDto(point);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPointMockMvc.perform(post("/api/points")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pointDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Point in the database
        List<Point> pointList = pointRepository.findAll();
        assertThat(pointList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllPoints() throws Exception {
        // Initialize the database
        pointRepository.saveAndFlush(point);

        // Get all the pointList
        restPointMockMvc.perform(get("/api/points?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(point.getId().intValue())))
            .andExpect(jsonPath("$.[*].outletId").value(hasItem(DEFAULT_OUTLET_ID.intValue())))
            .andExpect(jsonPath("$.[*].terminalId").value(hasItem(DEFAULT_TERMINAL_ID.intValue())))
            .andExpect(jsonPath("$.[*].merchantId").value(hasItem(DEFAULT_MERCHANT_ID.intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].campaignId").value(hasItem(DEFAULT_CAMPAIGN_ID.intValue())))
            .andExpect(jsonPath("$.[*].paymentTransaction").value(hasItem(DEFAULT_PAYMENT_TRANSACTION.intValue())))
            .andExpect(jsonPath("$.[*].paymentType").value(hasItem(DEFAULT_PAYMENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].point").value(hasItem(DEFAULT_POINT)));
    }

    @Test
    @Transactional
    public void getPoint() throws Exception {
        // Initialize the database
        pointRepository.saveAndFlush(point);

        // Get the point
        restPointMockMvc.perform(get("/api/points/{id}", point.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(point.getId().intValue()))
            .andExpect(jsonPath("$.outletId").value(DEFAULT_OUTLET_ID.intValue()))
            .andExpect(jsonPath("$.terminalId").value(DEFAULT_TERMINAL_ID.intValue()))
            .andExpect(jsonPath("$.merchantId").value(DEFAULT_MERCHANT_ID.intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()))
            .andExpect(jsonPath("$.campaignId").value(DEFAULT_CAMPAIGN_ID.intValue()))
            .andExpect(jsonPath("$.paymentTransaction").value(DEFAULT_PAYMENT_TRANSACTION.intValue()))
            .andExpect(jsonPath("$.paymentType").value(DEFAULT_PAYMENT_TYPE.toString()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.point").value(DEFAULT_POINT));
    }

    @Test
    @Transactional
    public void getAllPointsByOutletIdIsEqualToSomething() throws Exception {
        // Initialize the database
        pointRepository.saveAndFlush(point);

        // Get all the pointList where outletId equals to DEFAULT_OUTLET_ID
        defaultPointShouldBeFound("outletId.equals=" + DEFAULT_OUTLET_ID);

        // Get all the pointList where outletId equals to UPDATED_OUTLET_ID
        defaultPointShouldNotBeFound("outletId.equals=" + UPDATED_OUTLET_ID);
    }

    @Test
    @Transactional
    public void getAllPointsByOutletIdIsInShouldWork() throws Exception {
        // Initialize the database
        pointRepository.saveAndFlush(point);

        // Get all the pointList where outletId in DEFAULT_OUTLET_ID or UPDATED_OUTLET_ID
        defaultPointShouldBeFound("outletId.in=" + DEFAULT_OUTLET_ID + "," + UPDATED_OUTLET_ID);

        // Get all the pointList where outletId equals to UPDATED_OUTLET_ID
        defaultPointShouldNotBeFound("outletId.in=" + UPDATED_OUTLET_ID);
    }

    @Test
    @Transactional
    public void getAllPointsByOutletIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        pointRepository.saveAndFlush(point);

        // Get all the pointList where outletId is not null
        defaultPointShouldBeFound("outletId.specified=true");

        // Get all the pointList where outletId is null
        defaultPointShouldNotBeFound("outletId.specified=false");
    }

    @Test
    @Transactional
    public void getAllPointsByOutletIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pointRepository.saveAndFlush(point);

        // Get all the pointList where outletId greater than or equals to DEFAULT_OUTLET_ID
        defaultPointShouldBeFound("outletId.greaterOrEqualThan=" + DEFAULT_OUTLET_ID);

        // Get all the pointList where outletId greater than or equals to UPDATED_OUTLET_ID
        defaultPointShouldNotBeFound("outletId.greaterOrEqualThan=" + UPDATED_OUTLET_ID);
    }

    @Test
    @Transactional
    public void getAllPointsByOutletIdIsLessThanSomething() throws Exception {
        // Initialize the database
        pointRepository.saveAndFlush(point);

        // Get all the pointList where outletId less than or equals to DEFAULT_OUTLET_ID
        defaultPointShouldNotBeFound("outletId.lessThan=" + DEFAULT_OUTLET_ID);

        // Get all the pointList where outletId less than or equals to UPDATED_OUTLET_ID
        defaultPointShouldBeFound("outletId.lessThan=" + UPDATED_OUTLET_ID);
    }


    @Test
    @Transactional
    public void getAllPointsByTerminalIdIsEqualToSomething() throws Exception {
        // Initialize the database
        pointRepository.saveAndFlush(point);

        // Get all the pointList where terminalId equals to DEFAULT_TERMINAL_ID
        defaultPointShouldBeFound("terminalId.equals=" + DEFAULT_TERMINAL_ID);

        // Get all the pointList where terminalId equals to UPDATED_TERMINAL_ID
        defaultPointShouldNotBeFound("terminalId.equals=" + UPDATED_TERMINAL_ID);
    }

    @Test
    @Transactional
    public void getAllPointsByTerminalIdIsInShouldWork() throws Exception {
        // Initialize the database
        pointRepository.saveAndFlush(point);

        // Get all the pointList where terminalId in DEFAULT_TERMINAL_ID or UPDATED_TERMINAL_ID
        defaultPointShouldBeFound("terminalId.in=" + DEFAULT_TERMINAL_ID + "," + UPDATED_TERMINAL_ID);

        // Get all the pointList where terminalId equals to UPDATED_TERMINAL_ID
        defaultPointShouldNotBeFound("terminalId.in=" + UPDATED_TERMINAL_ID);
    }

    @Test
    @Transactional
    public void getAllPointsByTerminalIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        pointRepository.saveAndFlush(point);

        // Get all the pointList where terminalId is not null
        defaultPointShouldBeFound("terminalId.specified=true");

        // Get all the pointList where terminalId is null
        defaultPointShouldNotBeFound("terminalId.specified=false");
    }

    @Test
    @Transactional
    public void getAllPointsByTerminalIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pointRepository.saveAndFlush(point);

        // Get all the pointList where terminalId greater than or equals to DEFAULT_TERMINAL_ID
        defaultPointShouldBeFound("terminalId.greaterOrEqualThan=" + DEFAULT_TERMINAL_ID);

        // Get all the pointList where terminalId greater than or equals to UPDATED_TERMINAL_ID
        defaultPointShouldNotBeFound("terminalId.greaterOrEqualThan=" + UPDATED_TERMINAL_ID);
    }

    @Test
    @Transactional
    public void getAllPointsByTerminalIdIsLessThanSomething() throws Exception {
        // Initialize the database
        pointRepository.saveAndFlush(point);

        // Get all the pointList where terminalId less than or equals to DEFAULT_TERMINAL_ID
        defaultPointShouldNotBeFound("terminalId.lessThan=" + DEFAULT_TERMINAL_ID);

        // Get all the pointList where terminalId less than or equals to UPDATED_TERMINAL_ID
        defaultPointShouldBeFound("terminalId.lessThan=" + UPDATED_TERMINAL_ID);
    }


    @Test
    @Transactional
    public void getAllPointsByMerchantIdIsEqualToSomething() throws Exception {
        // Initialize the database
        pointRepository.saveAndFlush(point);

        // Get all the pointList where merchantId equals to DEFAULT_MERCHANT_ID
        defaultPointShouldBeFound("merchantId.equals=" + DEFAULT_MERCHANT_ID);

        // Get all the pointList where merchantId equals to UPDATED_MERCHANT_ID
        defaultPointShouldNotBeFound("merchantId.equals=" + UPDATED_MERCHANT_ID);
    }

    @Test
    @Transactional
    public void getAllPointsByMerchantIdIsInShouldWork() throws Exception {
        // Initialize the database
        pointRepository.saveAndFlush(point);

        // Get all the pointList where merchantId in DEFAULT_MERCHANT_ID or UPDATED_MERCHANT_ID
        defaultPointShouldBeFound("merchantId.in=" + DEFAULT_MERCHANT_ID + "," + UPDATED_MERCHANT_ID);

        // Get all the pointList where merchantId equals to UPDATED_MERCHANT_ID
        defaultPointShouldNotBeFound("merchantId.in=" + UPDATED_MERCHANT_ID);
    }

    @Test
    @Transactional
    public void getAllPointsByMerchantIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        pointRepository.saveAndFlush(point);

        // Get all the pointList where merchantId is not null
        defaultPointShouldBeFound("merchantId.specified=true");

        // Get all the pointList where merchantId is null
        defaultPointShouldNotBeFound("merchantId.specified=false");
    }

    @Test
    @Transactional
    public void getAllPointsByMerchantIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pointRepository.saveAndFlush(point);

        // Get all the pointList where merchantId greater than or equals to DEFAULT_MERCHANT_ID
        defaultPointShouldBeFound("merchantId.greaterOrEqualThan=" + DEFAULT_MERCHANT_ID);

        // Get all the pointList where merchantId greater than or equals to UPDATED_MERCHANT_ID
        defaultPointShouldNotBeFound("merchantId.greaterOrEqualThan=" + UPDATED_MERCHANT_ID);
    }

    @Test
    @Transactional
    public void getAllPointsByMerchantIdIsLessThanSomething() throws Exception {
        // Initialize the database
        pointRepository.saveAndFlush(point);

        // Get all the pointList where merchantId less than or equals to DEFAULT_MERCHANT_ID
        defaultPointShouldNotBeFound("merchantId.lessThan=" + DEFAULT_MERCHANT_ID);

        // Get all the pointList where merchantId less than or equals to UPDATED_MERCHANT_ID
        defaultPointShouldBeFound("merchantId.lessThan=" + UPDATED_MERCHANT_ID);
    }


    @Test
    @Transactional
    public void getAllPointsByUserIdIsEqualToSomething() throws Exception {
        // Initialize the database
        pointRepository.saveAndFlush(point);

        // Get all the pointList where userId equals to DEFAULT_USER_ID
        defaultPointShouldBeFound("userId.equals=" + DEFAULT_USER_ID);

        // Get all the pointList where userId equals to UPDATED_USER_ID
        defaultPointShouldNotBeFound("userId.equals=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    public void getAllPointsByUserIdIsInShouldWork() throws Exception {
        // Initialize the database
        pointRepository.saveAndFlush(point);

        // Get all the pointList where userId in DEFAULT_USER_ID or UPDATED_USER_ID
        defaultPointShouldBeFound("userId.in=" + DEFAULT_USER_ID + "," + UPDATED_USER_ID);

        // Get all the pointList where userId equals to UPDATED_USER_ID
        defaultPointShouldNotBeFound("userId.in=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    public void getAllPointsByUserIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        pointRepository.saveAndFlush(point);

        // Get all the pointList where userId is not null
        defaultPointShouldBeFound("userId.specified=true");

        // Get all the pointList where userId is null
        defaultPointShouldNotBeFound("userId.specified=false");
    }

    @Test
    @Transactional
    public void getAllPointsByUserIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pointRepository.saveAndFlush(point);

        // Get all the pointList where userId greater than or equals to DEFAULT_USER_ID
        defaultPointShouldBeFound("userId.greaterOrEqualThan=" + DEFAULT_USER_ID);

        // Get all the pointList where userId greater than or equals to UPDATED_USER_ID
        defaultPointShouldNotBeFound("userId.greaterOrEqualThan=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    public void getAllPointsByUserIdIsLessThanSomething() throws Exception {
        // Initialize the database
        pointRepository.saveAndFlush(point);

        // Get all the pointList where userId less than or equals to DEFAULT_USER_ID
        defaultPointShouldNotBeFound("userId.lessThan=" + DEFAULT_USER_ID);

        // Get all the pointList where userId less than or equals to UPDATED_USER_ID
        defaultPointShouldBeFound("userId.lessThan=" + UPDATED_USER_ID);
    }


    @Test
    @Transactional
    public void getAllPointsByCampaignIdIsEqualToSomething() throws Exception {
        // Initialize the database
        pointRepository.saveAndFlush(point);

        // Get all the pointList where campaignId equals to DEFAULT_CAMPAIGN_ID
        defaultPointShouldBeFound("campaignId.equals=" + DEFAULT_CAMPAIGN_ID);

        // Get all the pointList where campaignId equals to UPDATED_CAMPAIGN_ID
        defaultPointShouldNotBeFound("campaignId.equals=" + UPDATED_CAMPAIGN_ID);
    }

    @Test
    @Transactional
    public void getAllPointsByCampaignIdIsInShouldWork() throws Exception {
        // Initialize the database
        pointRepository.saveAndFlush(point);

        // Get all the pointList where campaignId in DEFAULT_CAMPAIGN_ID or UPDATED_CAMPAIGN_ID
        defaultPointShouldBeFound("campaignId.in=" + DEFAULT_CAMPAIGN_ID + "," + UPDATED_CAMPAIGN_ID);

        // Get all the pointList where campaignId equals to UPDATED_CAMPAIGN_ID
        defaultPointShouldNotBeFound("campaignId.in=" + UPDATED_CAMPAIGN_ID);
    }

    @Test
    @Transactional
    public void getAllPointsByCampaignIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        pointRepository.saveAndFlush(point);

        // Get all the pointList where campaignId is not null
        defaultPointShouldBeFound("campaignId.specified=true");

        // Get all the pointList where campaignId is null
        defaultPointShouldNotBeFound("campaignId.specified=false");
    }

    @Test
    @Transactional
    public void getAllPointsByCampaignIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pointRepository.saveAndFlush(point);

        // Get all the pointList where campaignId greater than or equals to DEFAULT_CAMPAIGN_ID
        defaultPointShouldBeFound("campaignId.greaterOrEqualThan=" + DEFAULT_CAMPAIGN_ID);

        // Get all the pointList where campaignId greater than or equals to UPDATED_CAMPAIGN_ID
        defaultPointShouldNotBeFound("campaignId.greaterOrEqualThan=" + UPDATED_CAMPAIGN_ID);
    }

    @Test
    @Transactional
    public void getAllPointsByCampaignIdIsLessThanSomething() throws Exception {
        // Initialize the database
        pointRepository.saveAndFlush(point);

        // Get all the pointList where campaignId less than or equals to DEFAULT_CAMPAIGN_ID
        defaultPointShouldNotBeFound("campaignId.lessThan=" + DEFAULT_CAMPAIGN_ID);

        // Get all the pointList where campaignId less than or equals to UPDATED_CAMPAIGN_ID
        defaultPointShouldBeFound("campaignId.lessThan=" + UPDATED_CAMPAIGN_ID);
    }


    @Test
    @Transactional
    public void getAllPointsByPaymentTransactionIsEqualToSomething() throws Exception {
        // Initialize the database
        pointRepository.saveAndFlush(point);

        // Get all the pointList where paymentTransaction equals to DEFAULT_PAYMENT_TRANSACTION
        defaultPointShouldBeFound("paymentTransaction.equals=" + DEFAULT_PAYMENT_TRANSACTION);

        // Get all the pointList where paymentTransaction equals to UPDATED_PAYMENT_TRANSACTION
        defaultPointShouldNotBeFound("paymentTransaction.equals=" + UPDATED_PAYMENT_TRANSACTION);
    }

    @Test
    @Transactional
    public void getAllPointsByPaymentTransactionIsInShouldWork() throws Exception {
        // Initialize the database
        pointRepository.saveAndFlush(point);

        // Get all the pointList where paymentTransaction in DEFAULT_PAYMENT_TRANSACTION or UPDATED_PAYMENT_TRANSACTION
        defaultPointShouldBeFound("paymentTransaction.in=" + DEFAULT_PAYMENT_TRANSACTION + "," + UPDATED_PAYMENT_TRANSACTION);

        // Get all the pointList where paymentTransaction equals to UPDATED_PAYMENT_TRANSACTION
        defaultPointShouldNotBeFound("paymentTransaction.in=" + UPDATED_PAYMENT_TRANSACTION);
    }

    @Test
    @Transactional
    public void getAllPointsByPaymentTransactionIsNullOrNotNull() throws Exception {
        // Initialize the database
        pointRepository.saveAndFlush(point);

        // Get all the pointList where paymentTransaction is not null
        defaultPointShouldBeFound("paymentTransaction.specified=true");

        // Get all the pointList where paymentTransaction is null
        defaultPointShouldNotBeFound("paymentTransaction.specified=false");
    }

    @Test
    @Transactional
    public void getAllPointsByPaymentTransactionIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pointRepository.saveAndFlush(point);

        // Get all the pointList where paymentTransaction greater than or equals to DEFAULT_PAYMENT_TRANSACTION
        defaultPointShouldBeFound("paymentTransaction.greaterOrEqualThan=" + DEFAULT_PAYMENT_TRANSACTION);

        // Get all the pointList where paymentTransaction greater than or equals to UPDATED_PAYMENT_TRANSACTION
        defaultPointShouldNotBeFound("paymentTransaction.greaterOrEqualThan=" + UPDATED_PAYMENT_TRANSACTION);
    }

    @Test
    @Transactional
    public void getAllPointsByPaymentTransactionIsLessThanSomething() throws Exception {
        // Initialize the database
        pointRepository.saveAndFlush(point);

        // Get all the pointList where paymentTransaction less than or equals to DEFAULT_PAYMENT_TRANSACTION
        defaultPointShouldNotBeFound("paymentTransaction.lessThan=" + DEFAULT_PAYMENT_TRANSACTION);

        // Get all the pointList where paymentTransaction less than or equals to UPDATED_PAYMENT_TRANSACTION
        defaultPointShouldBeFound("paymentTransaction.lessThan=" + UPDATED_PAYMENT_TRANSACTION);
    }


    @Test
    @Transactional
    public void getAllPointsByPaymentTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        pointRepository.saveAndFlush(point);

        // Get all the pointList where paymentType equals to DEFAULT_PAYMENT_TYPE
        defaultPointShouldBeFound("paymentType.equals=" + DEFAULT_PAYMENT_TYPE);

        // Get all the pointList where paymentType equals to UPDATED_PAYMENT_TYPE
        defaultPointShouldNotBeFound("paymentType.equals=" + UPDATED_PAYMENT_TYPE);
    }

    @Test
    @Transactional
    public void getAllPointsByPaymentTypeIsInShouldWork() throws Exception {
        // Initialize the database
        pointRepository.saveAndFlush(point);

        // Get all the pointList where paymentType in DEFAULT_PAYMENT_TYPE or UPDATED_PAYMENT_TYPE
        defaultPointShouldBeFound("paymentType.in=" + DEFAULT_PAYMENT_TYPE + "," + UPDATED_PAYMENT_TYPE);

        // Get all the pointList where paymentType equals to UPDATED_PAYMENT_TYPE
        defaultPointShouldNotBeFound("paymentType.in=" + UPDATED_PAYMENT_TYPE);
    }

    @Test
    @Transactional
    public void getAllPointsByPaymentTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        pointRepository.saveAndFlush(point);

        // Get all the pointList where paymentType is not null
        defaultPointShouldBeFound("paymentType.specified=true");

        // Get all the pointList where paymentType is null
        defaultPointShouldNotBeFound("paymentType.specified=false");
    }

    @Test
    @Transactional
    public void getAllPointsByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        pointRepository.saveAndFlush(point);

        // Get all the pointList where amount equals to DEFAULT_AMOUNT
        defaultPointShouldBeFound("amount.equals=" + DEFAULT_AMOUNT);

        // Get all the pointList where amount equals to UPDATED_AMOUNT
        defaultPointShouldNotBeFound("amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllPointsByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        pointRepository.saveAndFlush(point);

        // Get all the pointList where amount in DEFAULT_AMOUNT or UPDATED_AMOUNT
        defaultPointShouldBeFound("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT);

        // Get all the pointList where amount equals to UPDATED_AMOUNT
        defaultPointShouldNotBeFound("amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllPointsByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        pointRepository.saveAndFlush(point);

        // Get all the pointList where amount is not null
        defaultPointShouldBeFound("amount.specified=true");

        // Get all the pointList where amount is null
        defaultPointShouldNotBeFound("amount.specified=false");
    }

    @Test
    @Transactional
    public void getAllPointsByPointIsEqualToSomething() throws Exception {
        // Initialize the database
        pointRepository.saveAndFlush(point);

        // Get all the pointList where point equals to DEFAULT_POINT
        defaultPointShouldBeFound("point.equals=" + DEFAULT_POINT);

        // Get all the pointList where point equals to UPDATED_POINT
        defaultPointShouldNotBeFound("point.equals=" + UPDATED_POINT);
    }

    @Test
    @Transactional
    public void getAllPointsByPointIsInShouldWork() throws Exception {
        // Initialize the database
        pointRepository.saveAndFlush(point);

        // Get all the pointList where point in DEFAULT_POINT or UPDATED_POINT
        defaultPointShouldBeFound("point.in=" + DEFAULT_POINT + "," + UPDATED_POINT);

        // Get all the pointList where point equals to UPDATED_POINT
        defaultPointShouldNotBeFound("point.in=" + UPDATED_POINT);
    }

    @Test
    @Transactional
    public void getAllPointsByPointIsNullOrNotNull() throws Exception {
        // Initialize the database
        pointRepository.saveAndFlush(point);

        // Get all the pointList where point is not null
        defaultPointShouldBeFound("point.specified=true");

        // Get all the pointList where point is null
        defaultPointShouldNotBeFound("point.specified=false");
    }

    @Test
    @Transactional
    public void getAllPointsByPointIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pointRepository.saveAndFlush(point);

        // Get all the pointList where point greater than or equals to DEFAULT_POINT
        defaultPointShouldBeFound("point.greaterOrEqualThan=" + DEFAULT_POINT);

        // Get all the pointList where point greater than or equals to UPDATED_POINT
        defaultPointShouldNotBeFound("point.greaterOrEqualThan=" + UPDATED_POINT);
    }

    @Test
    @Transactional
    public void getAllPointsByPointIsLessThanSomething() throws Exception {
        // Initialize the database
        pointRepository.saveAndFlush(point);

        // Get all the pointList where point less than or equals to DEFAULT_POINT
        defaultPointShouldNotBeFound("point.lessThan=" + DEFAULT_POINT);

        // Get all the pointList where point less than or equals to UPDATED_POINT
        defaultPointShouldBeFound("point.lessThan=" + UPDATED_POINT);
    }


    @Test
    @Transactional
    public void getAllPointsByCampaignIsEqualToSomething() throws Exception {
        // Initialize the database
        Campaign campaign = CampaignResourceIntTest.createEntity(em);
        em.persist(campaign);
        em.flush();
        point.setCampaign(campaign);
        pointRepository.saveAndFlush(point);
        Long campaignId = campaign.getId();

        // Get all the pointList where campaign equals to campaignId
        defaultPointShouldBeFound("campaignId.equals=" + campaignId);

        // Get all the pointList where campaign equals to campaignId + 1
        defaultPointShouldNotBeFound("campaignId.equals=" + (campaignId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultPointShouldBeFound(String filter) throws Exception {
        restPointMockMvc.perform(get("/api/points?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(point.getId().intValue())))
            .andExpect(jsonPath("$.[*].outletId").value(hasItem(DEFAULT_OUTLET_ID.intValue())))
            .andExpect(jsonPath("$.[*].terminalId").value(hasItem(DEFAULT_TERMINAL_ID.intValue())))
            .andExpect(jsonPath("$.[*].merchantId").value(hasItem(DEFAULT_MERCHANT_ID.intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].campaignId").value(hasItem(DEFAULT_CAMPAIGN_ID.intValue())))
            .andExpect(jsonPath("$.[*].paymentTransaction").value(hasItem(DEFAULT_PAYMENT_TRANSACTION.intValue())))
            .andExpect(jsonPath("$.[*].paymentType").value(hasItem(DEFAULT_PAYMENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].point").value(hasItem(DEFAULT_POINT)));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultPointShouldNotBeFound(String filter) throws Exception {
        restPointMockMvc.perform(get("/api/points?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingPoint() throws Exception {
        // Get the point
        restPointMockMvc.perform(get("/api/points/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePoint() throws Exception {
        // Initialize the database
        pointRepository.saveAndFlush(point);
        int databaseSizeBeforeUpdate = pointRepository.findAll().size();

        // Update the point
        Point updatedPoint = pointRepository.findOne(point.getId());
        // Disconnect from session so that the updates on updatedPoint are not directly saved in db
        em.detach(updatedPoint);
        updatedPoint
            .outletId(UPDATED_OUTLET_ID)
            .terminalId(UPDATED_TERMINAL_ID)
            .merchantId(UPDATED_MERCHANT_ID)
            .userId(UPDATED_USER_ID)
            .campaignId(UPDATED_CAMPAIGN_ID)
            .paymentTransaction(UPDATED_PAYMENT_TRANSACTION)
            .paymentType(UPDATED_PAYMENT_TYPE)
            .amount(UPDATED_AMOUNT)
            .point(UPDATED_POINT);
        PointDTO pointDTO = pointMapper.toDto(updatedPoint);

        restPointMockMvc.perform(put("/api/points")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pointDTO)))
            .andExpect(status().isOk());

        // Validate the Point in the database
        List<Point> pointList = pointRepository.findAll();
        assertThat(pointList).hasSize(databaseSizeBeforeUpdate);
        Point testPoint = pointList.get(pointList.size() - 1);
        assertThat(testPoint.getOutletId()).isEqualTo(UPDATED_OUTLET_ID);
        assertThat(testPoint.getTerminalId()).isEqualTo(UPDATED_TERMINAL_ID);
        assertThat(testPoint.getMerchantId()).isEqualTo(UPDATED_MERCHANT_ID);
        assertThat(testPoint.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testPoint.getCampaignId()).isEqualTo(UPDATED_CAMPAIGN_ID);
        assertThat(testPoint.getPaymentTransaction()).isEqualTo(UPDATED_PAYMENT_TRANSACTION);
        assertThat(testPoint.getPaymentType()).isEqualTo(UPDATED_PAYMENT_TYPE);
        assertThat(testPoint.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testPoint.getPoint()).isEqualTo(UPDATED_POINT);
    }

    @Test
    @Transactional
    public void updateNonExistingPoint() throws Exception {
        int databaseSizeBeforeUpdate = pointRepository.findAll().size();

        // Create the Point
        PointDTO pointDTO = pointMapper.toDto(point);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPointMockMvc.perform(put("/api/points")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pointDTO)))
            .andExpect(status().isCreated());

        // Validate the Point in the database
        List<Point> pointList = pointRepository.findAll();
        assertThat(pointList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePoint() throws Exception {
        // Initialize the database
        pointRepository.saveAndFlush(point);
        int databaseSizeBeforeDelete = pointRepository.findAll().size();

        // Get the point
        restPointMockMvc.perform(delete("/api/points/{id}", point.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Point> pointList = pointRepository.findAll();
        assertThat(pointList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Point.class);
        Point point1 = new Point();
        point1.setId(1L);
        Point point2 = new Point();
        point2.setId(point1.getId());
        assertThat(point1).isEqualTo(point2);
        point2.setId(2L);
        assertThat(point1).isNotEqualTo(point2);
        point1.setId(null);
        assertThat(point1).isNotEqualTo(point2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PointDTO.class);
        PointDTO pointDTO1 = new PointDTO();
        pointDTO1.setId(1L);
        PointDTO pointDTO2 = new PointDTO();
        assertThat(pointDTO1).isNotEqualTo(pointDTO2);
        pointDTO2.setId(pointDTO1.getId());
        assertThat(pointDTO1).isEqualTo(pointDTO2);
        pointDTO2.setId(2L);
        assertThat(pointDTO1).isNotEqualTo(pointDTO2);
        pointDTO1.setId(null);
        assertThat(pointDTO1).isNotEqualTo(pointDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(pointMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(pointMapper.fromId(null)).isNull();
    }
}
