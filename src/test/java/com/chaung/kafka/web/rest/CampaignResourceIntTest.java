package com.chaung.kafka.web.rest;

import com.chaung.kafka.ChaungKafkaApp;

import com.chaung.kafka.domain.Campaign;
import com.chaung.kafka.repository.CampaignRepository;
import com.chaung.kafka.service.CampaignService;
import com.chaung.kafka.service.dto.CampaignDTO;
import com.chaung.kafka.service.mapper.CampaignMapper;
import com.chaung.kafka.web.rest.errors.ExceptionTranslator;
import com.chaung.kafka.service.dto.CampaignCriteria;
import com.chaung.kafka.service.CampaignQueryService;

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

import com.chaung.kafka.domain.enumeration.UserType;
/**
 * Test class for the CampaignResource REST controller.
 *
 * @see CampaignResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ChaungKafkaApp.class)
public class CampaignResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Double DEFAULT_MIN_AMOUNT = 1D;
    private static final Double UPDATED_MIN_AMOUNT = 2D;

    private static final Double DEFAULT_MAX_AMOUNT = 1D;
    private static final Double UPDATED_MAX_AMOUNT = 2D;

    private static final Integer DEFAULT_POINT = 1;
    private static final Integer UPDATED_POINT = 2;

    private static final UserType DEFAULT_TYPE = UserType.CUSTOMER;
    private static final UserType UPDATED_TYPE = UserType.MERCHANT;

    @Autowired
    private CampaignRepository campaignRepository;

    @Autowired
    private CampaignMapper campaignMapper;

    @Autowired
    private CampaignService campaignService;

    @Autowired
    private CampaignQueryService campaignQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCampaignMockMvc;

    private Campaign campaign;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CampaignResource campaignResource = new CampaignResource(campaignService, campaignQueryService);
        this.restCampaignMockMvc = MockMvcBuilders.standaloneSetup(campaignResource)
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
    public static Campaign createEntity(EntityManager em) {
        Campaign campaign = new Campaign()
            .name(DEFAULT_NAME)
            .minAmount(DEFAULT_MIN_AMOUNT)
            .maxAmount(DEFAULT_MAX_AMOUNT)
            .point(DEFAULT_POINT)
            .type(DEFAULT_TYPE);
        return campaign;
    }

    @Before
    public void initTest() {
        campaign = createEntity(em);
    }

    @Test
    @Transactional
    public void createCampaign() throws Exception {
        int databaseSizeBeforeCreate = campaignRepository.findAll().size();

        // Create the Campaign
        CampaignDTO campaignDTO = campaignMapper.toDto(campaign);
        restCampaignMockMvc.perform(post("/api/campaigns")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(campaignDTO)))
            .andExpect(status().isCreated());

        // Validate the Campaign in the database
        List<Campaign> campaignList = campaignRepository.findAll();
        assertThat(campaignList).hasSize(databaseSizeBeforeCreate + 1);
        Campaign testCampaign = campaignList.get(campaignList.size() - 1);
        assertThat(testCampaign.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCampaign.getMinAmount()).isEqualTo(DEFAULT_MIN_AMOUNT);
        assertThat(testCampaign.getMaxAmount()).isEqualTo(DEFAULT_MAX_AMOUNT);
        assertThat(testCampaign.getPoint()).isEqualTo(DEFAULT_POINT);
        assertThat(testCampaign.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    public void createCampaignWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = campaignRepository.findAll().size();

        // Create the Campaign with an existing ID
        campaign.setId(1L);
        CampaignDTO campaignDTO = campaignMapper.toDto(campaign);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCampaignMockMvc.perform(post("/api/campaigns")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(campaignDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Campaign in the database
        List<Campaign> campaignList = campaignRepository.findAll();
        assertThat(campaignList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCampaigns() throws Exception {
        // Initialize the database
        campaignRepository.saveAndFlush(campaign);

        // Get all the campaignList
        restCampaignMockMvc.perform(get("/api/campaigns?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(campaign.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].minAmount").value(hasItem(DEFAULT_MIN_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].maxAmount").value(hasItem(DEFAULT_MAX_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].point").value(hasItem(DEFAULT_POINT)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }

    @Test
    @Transactional
    public void getCampaign() throws Exception {
        // Initialize the database
        campaignRepository.saveAndFlush(campaign);

        // Get the campaign
        restCampaignMockMvc.perform(get("/api/campaigns/{id}", campaign.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(campaign.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.minAmount").value(DEFAULT_MIN_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.maxAmount").value(DEFAULT_MAX_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.point").value(DEFAULT_POINT))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getAllCampaignsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        campaignRepository.saveAndFlush(campaign);

        // Get all the campaignList where name equals to DEFAULT_NAME
        defaultCampaignShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the campaignList where name equals to UPDATED_NAME
        defaultCampaignShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllCampaignsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        campaignRepository.saveAndFlush(campaign);

        // Get all the campaignList where name in DEFAULT_NAME or UPDATED_NAME
        defaultCampaignShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the campaignList where name equals to UPDATED_NAME
        defaultCampaignShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllCampaignsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        campaignRepository.saveAndFlush(campaign);

        // Get all the campaignList where name is not null
        defaultCampaignShouldBeFound("name.specified=true");

        // Get all the campaignList where name is null
        defaultCampaignShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllCampaignsByMinAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        campaignRepository.saveAndFlush(campaign);

        // Get all the campaignList where minAmount equals to DEFAULT_MIN_AMOUNT
        defaultCampaignShouldBeFound("minAmount.equals=" + DEFAULT_MIN_AMOUNT);

        // Get all the campaignList where minAmount equals to UPDATED_MIN_AMOUNT
        defaultCampaignShouldNotBeFound("minAmount.equals=" + UPDATED_MIN_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllCampaignsByMinAmountIsInShouldWork() throws Exception {
        // Initialize the database
        campaignRepository.saveAndFlush(campaign);

        // Get all the campaignList where minAmount in DEFAULT_MIN_AMOUNT or UPDATED_MIN_AMOUNT
        defaultCampaignShouldBeFound("minAmount.in=" + DEFAULT_MIN_AMOUNT + "," + UPDATED_MIN_AMOUNT);

        // Get all the campaignList where minAmount equals to UPDATED_MIN_AMOUNT
        defaultCampaignShouldNotBeFound("minAmount.in=" + UPDATED_MIN_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllCampaignsByMinAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        campaignRepository.saveAndFlush(campaign);

        // Get all the campaignList where minAmount is not null
        defaultCampaignShouldBeFound("minAmount.specified=true");

        // Get all the campaignList where minAmount is null
        defaultCampaignShouldNotBeFound("minAmount.specified=false");
    }

    @Test
    @Transactional
    public void getAllCampaignsByMaxAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        campaignRepository.saveAndFlush(campaign);

        // Get all the campaignList where maxAmount equals to DEFAULT_MAX_AMOUNT
        defaultCampaignShouldBeFound("maxAmount.equals=" + DEFAULT_MAX_AMOUNT);

        // Get all the campaignList where maxAmount equals to UPDATED_MAX_AMOUNT
        defaultCampaignShouldNotBeFound("maxAmount.equals=" + UPDATED_MAX_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllCampaignsByMaxAmountIsInShouldWork() throws Exception {
        // Initialize the database
        campaignRepository.saveAndFlush(campaign);

        // Get all the campaignList where maxAmount in DEFAULT_MAX_AMOUNT or UPDATED_MAX_AMOUNT
        defaultCampaignShouldBeFound("maxAmount.in=" + DEFAULT_MAX_AMOUNT + "," + UPDATED_MAX_AMOUNT);

        // Get all the campaignList where maxAmount equals to UPDATED_MAX_AMOUNT
        defaultCampaignShouldNotBeFound("maxAmount.in=" + UPDATED_MAX_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllCampaignsByMaxAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        campaignRepository.saveAndFlush(campaign);

        // Get all the campaignList where maxAmount is not null
        defaultCampaignShouldBeFound("maxAmount.specified=true");

        // Get all the campaignList where maxAmount is null
        defaultCampaignShouldNotBeFound("maxAmount.specified=false");
    }

    @Test
    @Transactional
    public void getAllCampaignsByPointIsEqualToSomething() throws Exception {
        // Initialize the database
        campaignRepository.saveAndFlush(campaign);

        // Get all the campaignList where point equals to DEFAULT_POINT
        defaultCampaignShouldBeFound("point.equals=" + DEFAULT_POINT);

        // Get all the campaignList where point equals to UPDATED_POINT
        defaultCampaignShouldNotBeFound("point.equals=" + UPDATED_POINT);
    }

    @Test
    @Transactional
    public void getAllCampaignsByPointIsInShouldWork() throws Exception {
        // Initialize the database
        campaignRepository.saveAndFlush(campaign);

        // Get all the campaignList where point in DEFAULT_POINT or UPDATED_POINT
        defaultCampaignShouldBeFound("point.in=" + DEFAULT_POINT + "," + UPDATED_POINT);

        // Get all the campaignList where point equals to UPDATED_POINT
        defaultCampaignShouldNotBeFound("point.in=" + UPDATED_POINT);
    }

    @Test
    @Transactional
    public void getAllCampaignsByPointIsNullOrNotNull() throws Exception {
        // Initialize the database
        campaignRepository.saveAndFlush(campaign);

        // Get all the campaignList where point is not null
        defaultCampaignShouldBeFound("point.specified=true");

        // Get all the campaignList where point is null
        defaultCampaignShouldNotBeFound("point.specified=false");
    }

    @Test
    @Transactional
    public void getAllCampaignsByPointIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        campaignRepository.saveAndFlush(campaign);

        // Get all the campaignList where point greater than or equals to DEFAULT_POINT
        defaultCampaignShouldBeFound("point.greaterOrEqualThan=" + DEFAULT_POINT);

        // Get all the campaignList where point greater than or equals to UPDATED_POINT
        defaultCampaignShouldNotBeFound("point.greaterOrEqualThan=" + UPDATED_POINT);
    }

    @Test
    @Transactional
    public void getAllCampaignsByPointIsLessThanSomething() throws Exception {
        // Initialize the database
        campaignRepository.saveAndFlush(campaign);

        // Get all the campaignList where point less than or equals to DEFAULT_POINT
        defaultCampaignShouldNotBeFound("point.lessThan=" + DEFAULT_POINT);

        // Get all the campaignList where point less than or equals to UPDATED_POINT
        defaultCampaignShouldBeFound("point.lessThan=" + UPDATED_POINT);
    }


    @Test
    @Transactional
    public void getAllCampaignsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        campaignRepository.saveAndFlush(campaign);

        // Get all the campaignList where type equals to DEFAULT_TYPE
        defaultCampaignShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the campaignList where type equals to UPDATED_TYPE
        defaultCampaignShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllCampaignsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        campaignRepository.saveAndFlush(campaign);

        // Get all the campaignList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultCampaignShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the campaignList where type equals to UPDATED_TYPE
        defaultCampaignShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllCampaignsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        campaignRepository.saveAndFlush(campaign);

        // Get all the campaignList where type is not null
        defaultCampaignShouldBeFound("type.specified=true");

        // Get all the campaignList where type is null
        defaultCampaignShouldNotBeFound("type.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultCampaignShouldBeFound(String filter) throws Exception {
        restCampaignMockMvc.perform(get("/api/campaigns?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(campaign.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].minAmount").value(hasItem(DEFAULT_MIN_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].maxAmount").value(hasItem(DEFAULT_MAX_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].point").value(hasItem(DEFAULT_POINT)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultCampaignShouldNotBeFound(String filter) throws Exception {
        restCampaignMockMvc.perform(get("/api/campaigns?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingCampaign() throws Exception {
        // Get the campaign
        restCampaignMockMvc.perform(get("/api/campaigns/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCampaign() throws Exception {
        // Initialize the database
        campaignRepository.saveAndFlush(campaign);
        int databaseSizeBeforeUpdate = campaignRepository.findAll().size();

        // Update the campaign
        Campaign updatedCampaign = campaignRepository.findOne(campaign.getId());
        // Disconnect from session so that the updates on updatedCampaign are not directly saved in db
        em.detach(updatedCampaign);
        updatedCampaign
            .name(UPDATED_NAME)
            .minAmount(UPDATED_MIN_AMOUNT)
            .maxAmount(UPDATED_MAX_AMOUNT)
            .point(UPDATED_POINT)
            .type(UPDATED_TYPE);
        CampaignDTO campaignDTO = campaignMapper.toDto(updatedCampaign);

        restCampaignMockMvc.perform(put("/api/campaigns")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(campaignDTO)))
            .andExpect(status().isOk());

        // Validate the Campaign in the database
        List<Campaign> campaignList = campaignRepository.findAll();
        assertThat(campaignList).hasSize(databaseSizeBeforeUpdate);
        Campaign testCampaign = campaignList.get(campaignList.size() - 1);
        assertThat(testCampaign.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCampaign.getMinAmount()).isEqualTo(UPDATED_MIN_AMOUNT);
        assertThat(testCampaign.getMaxAmount()).isEqualTo(UPDATED_MAX_AMOUNT);
        assertThat(testCampaign.getPoint()).isEqualTo(UPDATED_POINT);
        assertThat(testCampaign.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingCampaign() throws Exception {
        int databaseSizeBeforeUpdate = campaignRepository.findAll().size();

        // Create the Campaign
        CampaignDTO campaignDTO = campaignMapper.toDto(campaign);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCampaignMockMvc.perform(put("/api/campaigns")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(campaignDTO)))
            .andExpect(status().isCreated());

        // Validate the Campaign in the database
        List<Campaign> campaignList = campaignRepository.findAll();
        assertThat(campaignList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCampaign() throws Exception {
        // Initialize the database
        campaignRepository.saveAndFlush(campaign);
        int databaseSizeBeforeDelete = campaignRepository.findAll().size();

        // Get the campaign
        restCampaignMockMvc.perform(delete("/api/campaigns/{id}", campaign.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Campaign> campaignList = campaignRepository.findAll();
        assertThat(campaignList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Campaign.class);
        Campaign campaign1 = new Campaign();
        campaign1.setId(1L);
        Campaign campaign2 = new Campaign();
        campaign2.setId(campaign1.getId());
        assertThat(campaign1).isEqualTo(campaign2);
        campaign2.setId(2L);
        assertThat(campaign1).isNotEqualTo(campaign2);
        campaign1.setId(null);
        assertThat(campaign1).isNotEqualTo(campaign2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CampaignDTO.class);
        CampaignDTO campaignDTO1 = new CampaignDTO();
        campaignDTO1.setId(1L);
        CampaignDTO campaignDTO2 = new CampaignDTO();
        assertThat(campaignDTO1).isNotEqualTo(campaignDTO2);
        campaignDTO2.setId(campaignDTO1.getId());
        assertThat(campaignDTO1).isEqualTo(campaignDTO2);
        campaignDTO2.setId(2L);
        assertThat(campaignDTO1).isNotEqualTo(campaignDTO2);
        campaignDTO1.setId(null);
        assertThat(campaignDTO1).isNotEqualTo(campaignDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(campaignMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(campaignMapper.fromId(null)).isNull();
    }
}
