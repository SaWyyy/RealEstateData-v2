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
import pl.pollub.integracja_projekt.Controllers.InterestRatesController;
import pl.pollub.integracja_projekt.Models.InterestRates;
import pl.pollub.integracja_projekt.Services.InterestRatesService;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;


import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class InterestRatesControllerTests {
    private MockMvc mockMvc;

    @Mock
    private InterestRatesService interestRatesService;

    @InjectMocks
    private InterestRatesController interestRatesController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(interestRatesController).build();
    }

    @Test
    public void InterestRatesController_Create_ShouldReturnOk() throws Exception {
        InterestRates interestRates = new InterestRates(
                1, new Date().toString(), 1.0, 1.0, 1.0, 1.0, 1.0
        );

        when(interestRatesService.addInterestRates(interestRates)).thenReturn(interestRates);

        mockMvc.perform(post("/api/interestRates/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(interestRates)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(interestRates.getId()))
                .andExpect(jsonPath("$.date").value(interestRates.getDate()))
                .andExpect(jsonPath("$.refRate").value(interestRates.getRefRate()))
                .andExpect(jsonPath("$.pawnRate").value(interestRates.getPawnRate()))
                .andExpect(jsonPath("$.depRate").value(interestRates.getDepRate()))
                .andExpect(jsonPath("$.redRate").value(interestRates.getRedRate()))
                .andExpect(jsonPath("$.disRate").value(interestRates.getDisRate()));
    }

    @Test
    public void InterestRatesController_Create_ShouldReturnBadRequest() throws Exception {
        InterestRates interestRates = new InterestRates();

        when(interestRatesService.addInterestRates(interestRates)).thenThrow(new IllegalArgumentException());

        mockMvc.perform(post("/api/interestRates/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(interestRates)))
                .andExpect(status().isBadRequest());
    }


    @Test
    public void InterestRatesController_GetAllRates_ShouldReturnOk() throws Exception {
        List<InterestRates> interestRates = new ArrayList<InterestRates>();
        when(interestRatesService.getInterestRatesByDateRange(new Date().toString(), new Date().toString() )).thenReturn(interestRates);
        mockMvc.perform(get("/api/interestRates/?fromDate=test&toDate=test"))
                .andExpect(status().isOk());
    }

   @Test
    public void InterestRatesController_Delete_ShouldReturnOk() throws Exception {
       when(interestRatesService.deleteInterestRates(1)).thenReturn(new InterestRates());

       mockMvc.perform(delete("/api/interestRates/1"))
               .andExpect(status().isOk());
   }

   @Test
   public void InterestRatesController_Delete_ShouldReturnBadRequest() throws Exception {
       when(interestRatesService.deleteInterestRates(null)).thenThrow(new IllegalArgumentException());

       mockMvc.perform(delete("/api/interestRates/a"))
               .andExpect(status().isBadRequest());
   }

    @Test
    public void InterestRatesController_Update_ShouldReturnOk() throws Exception {
        InterestRates interestRates = new InterestRates(
                1, new Date().toString(), 1.0, 1.0, 1.0, 1.0, 1.0
        );

        when(interestRatesService.updateInterestRates(1, interestRates)).thenReturn(interestRates);

        mockMvc.perform(put("/api/interestRates/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(interestRates)))
                .andExpect(status().isOk());
    }

    @Test
    public void InterestRatesController_Update_ShouldReturnBadRequest() throws Exception {
        InterestRates interestRates = new InterestRates();

        when(interestRatesService.updateInterestRates(1, interestRates)).thenThrow(new NoSuchElementException());

        mockMvc.perform(put("/api/interestRates/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(interestRates)))
                .andExpect(status().isBadRequest());
    }

}