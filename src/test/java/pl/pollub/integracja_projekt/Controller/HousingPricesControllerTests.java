package pl.pollub.integracja_projekt.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.pollub.integracja_projekt.Config.AuthenticationResponse;
import pl.pollub.integracja_projekt.Controllers.AuthenticationController;
import pl.pollub.integracja_projekt.Controllers.HousingPricesController;
import pl.pollub.integracja_projekt.Models.HousingPrices;
import pl.pollub.integracja_projekt.Services.AuthenticationService;
import pl.pollub.integracja_projekt.Services.HousingPricesService;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class HousingPricesControllerTests {
    private MockMvc mockMvc;

    @Mock
    private HousingPricesService housingPricesService;

    @InjectMocks
    private HousingPricesController housingPricesController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(housingPricesController).build();
    }

    @Test
    public void HousingPricesController_Create_ShouldReturnOk() throws Exception{
        HousingPrices housingPrices = new HousingPrices(
                1, "name", "transaction", "surface", 2000, 15000
        );

        when(housingPricesService.addHousingPrices(housingPrices)).thenReturn(housingPrices);

        mockMvc.perform(post("/api/housingPrices/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(housingPrices)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("name"))
                .andExpect(jsonPath("$.transaction").value("transaction"))
                .andExpect(jsonPath("$.surface").value("surface"))
                .andExpect(jsonPath("$.year").value(2000))
                .andExpect(jsonPath("$.price").value(15000));
    }

    @Test
    public void HousingPricesController_Create_ShouldReturnBadRequest() throws Exception{
        HousingPrices request = new HousingPrices();


        when(housingPricesService.addHousingPrices(request)).thenThrow(new IllegalArgumentException());

        mockMvc.perform(post("/api/housingPrices/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void HousingPricesController_getHousingPrices_ShouldReturnOk() throws Exception{
        List<HousingPrices> housingPricesList = new ArrayList<HousingPrices>();

        when(housingPricesService.getHousingPrices(20, "example", "example", "example")).thenReturn(housingPricesList);

        mockMvc.perform(get("/api/housingPrices/"))
                .andExpect(status().isOk());
    }

    @Test
    public void HousingPricesController_getHousingPrices_ShouldReturnBadRequest() throws Exception{
        List<HousingPrices> housingPricesList = new ArrayList<>();

        when(housingPricesService.getHousingPricesByYear(0)).thenThrow(new IllegalArgumentException("Year cant be less than  or euqal 0"));

        mockMvc.perform(get("/api/housingPrices/?year=0"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void HousingPricesController_deleteHousingPrices_ShouldReturnOk() throws Exception{
        when(housingPricesService.deleteHousingPrices(1)).thenReturn(new HousingPrices());

        mockMvc.perform(delete("/api/housingPrices/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void HousingPricesController_deleteHousingPrices_ShouldReturnBadRequest() throws Exception{
        when(housingPricesService.deleteHousingPrices(null)).thenThrow(new IllegalArgumentException());

        mockMvc.perform(delete("/api/housingPrices/null"))
                .andExpect(status().isBadRequest());
    }


    @Test
    public void HousingPricesController_UpdateHousingPrices_ShouldReturnOk() throws Exception{
        HousingPrices housingPrices = new HousingPrices(1, "name", "transaction", "surface", 2000, 15000);

        when(housingPricesService.updateHousingPrices(1, housingPrices)).thenReturn(housingPrices);

        mockMvc.perform(put("/api/housingPrices/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(housingPrices)))
                .andExpect(status().isOk());
    }

    @Test
    public void HousingPricesController_UpdateHousingPrices_ShouldReturnBadRequest() throws Exception{
        HousingPrices request = new HousingPrices(1, "name", "transaction", "surface", 2000, 15000);

        when(housingPricesService.updateHousingPrices(1, request)).thenThrow(new NoSuchElementException("Not found"));

        mockMvc.perform(put("/api/housingPrices/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
