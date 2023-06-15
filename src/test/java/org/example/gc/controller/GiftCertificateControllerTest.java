package org.example.gc.controller;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

@ExtendWith(SpringExtension.class)

@WebAppConfiguration
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GiftCertificateControllerTest {
    /*
    private static final String URL = "http://localhost:8080/gift-certificates";
    private static final String NAME = RandomStringUtils.randomAlphanumeric(10);

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private GiftCertificateDao giftCertificateDao;
    @Autowired
    private TagDao tagDao;
    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void testGetAll_withoutParameters() throws Exception {
        mockMvc.perform(get(URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(new Gson().toJson(giftCertificateDao.getAll(new GiftCertificateParametersHandler(null)))));
    }

    @ParameterizedTest
    @CsvSource({"1,1", "2,1", "2,3"})
    void testGetAll_withValidPaginationParameters(int limit, int page) throws Exception {
        GiftCertificateParameters parameters = new GiftCertificateParameters();
        parameters.setLimit(limit);
        parameters.setPage(page);
        mockMvc.perform(get(URL)
                        .param("limit", String.valueOf(limit))
                        .param("page", String.valueOf(page)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(new Gson().toJson(giftCertificateDao.getAll(new GiftCertificateParametersHandler(parameters)))));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 6})
    void testGetAll_withValidPaginationParametersWithoutPageSpecified(int limit) throws Exception {
        GiftCertificateParameters parameters = new GiftCertificateParameters();
        parameters.setLimit(limit);
        mockMvc.perform(get(URL).param("limit", String.valueOf(limit)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(new Gson().toJson(giftCertificateDao.getAll(new GiftCertificateParametersHandler(parameters)))));

    }

    @ParameterizedTest
    @CsvSource({"1,0", "1,f", "0,1", ",1", "l,1", "0,"})
    void testGetAll_whenInvalidPaginationParameters_shouldResponseStatusBadRequest
            (String limit, String page) throws Exception {
        mockMvc.perform(get(URL)
                        .param("limit", limit)
                        .param("page", page))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @CsvSource(value = {"name,asc", "name,desc", "date,desc","date,asc"}, delimiter = ':')
    void testGetAll_withValidOneSortParameter(String parameter) throws Exception {
        GiftCertificateParameters parameters = new GiftCertificateParameters();
        parameters.setSort(new String[]{parameter});
        mockMvc.perform(get(URL).param("sort", parameter))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(new Gson().toJson(giftCertificateDao.getAll(new GiftCertificateParametersHandler(parameters)))));
    }

    @ParameterizedTest
    @MethodSource("stringArrayProviderOfTwoSortTypes")
    void testGetAll_withValidTwoSortParameters(String[] parametersArray) throws Exception {
        GiftCertificateParameters parameters = new GiftCertificateParameters();
        parameters.setSort(parametersArray);
        mockMvc.perform(get(URL).param("sort", parametersArray[0]).param("sort", parametersArray[1]))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(new Gson().toJson(giftCertificateDao.getAll(new GiftCertificateParametersHandler(parameters)))));
    }

    private static Stream<Arguments> stringArrayProviderOfTwoSortTypes() {
        return Stream.of(
                Arguments.of((Object) new String[]{"name,asc", "date,desc"}),
                Arguments.of((Object) new String[]{"name,desc", "date,desc"}),
                Arguments.of((Object) new String[]{"name,asc", "date,asc"}),
                Arguments.of((Object) new String[]{"name,desc", "date,asc"})
        );
    }

    @Test
    void testGetById_whenIdExists() {
        giftCertificateDao.getAll(new GiftCertificateParametersHandler(null)).stream().limit(10).forEach(gs -> {
            try {
                mockMvc.perform(get(URL + "/" + gs.getId()))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(content().string(new Gson().toJson(gs)));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    void testGetById_whenIdDoesNotExist() throws Exception {
        mockMvc.perform(get(URL + "/" + 0))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetById_whenIdIsNotValid() throws Exception {
        mockMvc.perform(get(URL + "/f"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(1)
    void testPost_whenGiftCertificateIsValidWithExistingTag() throws Exception {

        tagDao.insert(new Tag(0L, NAME));
        TagRequestDto tagRequestDto = new TagRequestDto();
        tagRequestDto.setName(NAME);
        Collection<TagRequestDto> tags = new ArrayList<>();
        tags.add(tagRequestDto);
        GiftCertificateRequestInsertDto giftCertificateRequestDto = new GiftCertificateRequestInsertDto();
        giftCertificateRequestDto.setName(NAME);
        giftCertificateRequestDto.setDescription(RandomStringUtils.randomAlphanumeric(20));
        giftCertificateRequestDto.setDuration(10);
        giftCertificateRequestDto.setPrice(20.25);
        giftCertificateRequestDto.setTags(tags);
        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsBytes(giftCertificateRequestDto)))
                .andExpect(status().isCreated());
        GiftCertificate gc = giftCertificateDao.getByName(NAME);
        assertEquals(NAME, gc.getName());
        assertEquals(10, gc.getDuration());
        assertEquals(20.25, gc.getPrice());


    }

    @Test
    @Order(4)
    void testPost_whenGiftCertificateIsValidWithNotExistingTag() throws Exception {
        TagRequestDto tagRequestDto = new TagRequestDto();
        tagRequestDto.setName(NAME);
        Collection<TagRequestDto> tags = new ArrayList<>();
        tags.add(tagRequestDto);
        GiftCertificateRequestInsertDto giftCertificateRequestDto = new GiftCertificateRequestInsertDto();
        giftCertificateRequestDto.setName(NAME);
        giftCertificateRequestDto.setDescription(RandomStringUtils.randomAlphanumeric(20));
        giftCertificateRequestDto.setDuration(10);
        giftCertificateRequestDto.setPrice(20.25);
        giftCertificateRequestDto.setTags(tags);
        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsBytes(giftCertificateRequestDto)))
                .andExpect(status().isCreated());
        GiftCertificate gc = giftCertificateDao.getByName(NAME);
        assertEquals(NAME, gc.getName());
        assertEquals(10, gc.getDuration());
        assertEquals(20.25, gc.getPrice());
        assertNotNull(tagDao.getByName(NAME));
        giftCertificateDao.delete(gc.getId());
        tagDao.delete(tagDao.getByName(NAME).getId());
    }

    @Test
    @Order(2)
    void testPost_whenGiftCertificateNameExists() throws Exception {
        GiftCertificateRequestInsertDto giftCertificateRequestDto = new GiftCertificateRequestInsertDto();
        giftCertificateRequestDto.setName(NAME);
        giftCertificateRequestDto.setDescription(RandomStringUtils.randomAlphanumeric(20));
        giftCertificateRequestDto.setDuration(10);
        giftCertificateRequestDto.setPrice(20.25);
        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsBytes(giftCertificateRequestDto)))
                .andExpect(status().isBadRequest());
    }


    @Test
    void testPost_whenPriceIsNull() throws Exception {
        GiftCertificateRequestInsertDto giftCertificateRequestDto = new GiftCertificateRequestInsertDto();
        giftCertificateRequestDto.setName(NAME);
        giftCertificateRequestDto.setDescription(RandomStringUtils.randomAlphanumeric(20));
        giftCertificateRequestDto.setDuration(10);
        giftCertificateRequestDto.setPrice(null);
        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsBytes(giftCertificateRequestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testPost_whenPriceIsNegative() throws Exception {
        GiftCertificateRequestInsertDto giftCertificateRequestDto = new GiftCertificateRequestInsertDto();
        giftCertificateRequestDto.setName(NAME);
        giftCertificateRequestDto.setDescription(RandomStringUtils.randomAlphanumeric(20));
        giftCertificateRequestDto.setDuration(10);
        giftCertificateRequestDto.setPrice(-2.0);
        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsBytes(giftCertificateRequestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testPost_whenDurationIsNegative() throws Exception {
        GiftCertificateRequestInsertDto giftCertificateRequestDto = new GiftCertificateRequestInsertDto();
        giftCertificateRequestDto.setName(NAME);
        giftCertificateRequestDto.setDescription(RandomStringUtils.randomAlphanumeric(20));
        giftCertificateRequestDto.setDuration(-10);
        giftCertificateRequestDto.setPrice(2.0);
        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsBytes(giftCertificateRequestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testPost_whenDurationIsNull() throws Exception {
        GiftCertificateRequestInsertDto giftCertificateRequestDto = new GiftCertificateRequestInsertDto();
        giftCertificateRequestDto.setName(NAME);
        giftCertificateRequestDto.setDescription(RandomStringUtils.randomAlphanumeric(20));
        giftCertificateRequestDto.setDuration(null);
        giftCertificateRequestDto.setPrice(2.0);
        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsBytes(giftCertificateRequestDto)))
                .andExpect(status().isBadRequest());
    }


    @Test
    void testPost_whenDurationIsZero() throws Exception {
        GiftCertificateRequestInsertDto giftCertificateRequestDto = new GiftCertificateRequestInsertDto();
        giftCertificateRequestDto.setName(NAME);
        giftCertificateRequestDto.setDescription(RandomStringUtils.randomAlphanumeric(20));
        giftCertificateRequestDto.setDuration(0);
        giftCertificateRequestDto.setPrice(2.0);
        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsBytes(giftCertificateRequestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testPost_whenPriceIsZero() throws Exception {
        GiftCertificateRequestInsertDto giftCertificateRequestDto = new GiftCertificateRequestInsertDto();
        giftCertificateRequestDto.setName(NAME);
        giftCertificateRequestDto.setDescription(RandomStringUtils.randomAlphanumeric(20));
        giftCertificateRequestDto.setDuration(10);
        giftCertificateRequestDto.setPrice(0.0);
        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsBytes(giftCertificateRequestDto)))
                .andExpect(status().isBadRequest());
    }


    @Test
    @Order(3)
    void testPut_whenGiftCertificateExists() throws Exception {
        //TODO
    }

    @Test
    void testPut_whenGiftCertificateDoesNotExists() throws Exception {
        //TODO
    }

    @Test
    void testPut_whenParametersAreNotValid() throws Exception {
        //TODO
    }


    @Test
    @Order(3)
    void testDelete_whenGiftCertificateExists() throws Exception {
        long id = giftCertificateDao.getByName(NAME).getId();
        mockMvc.perform(delete(URL + "/" + id)).andExpect(status().isNoContent());
        assertNull(giftCertificateDao.getByName(NAME));
        tagDao.delete(tagDao.getByName(NAME).getId());
    }

    @Test
    void testDelete_whenGiftCertificateDoesNotExist() throws Exception {
        mockMvc.perform(delete(URL + "/" + 0))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDelete_whenIdIsNotValid() throws Exception {
        mockMvc.perform(delete(URL + "/l"))
                .andExpect(status().isBadRequest());
    }

     */
}