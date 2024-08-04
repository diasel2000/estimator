package com.estimator.controller;


import com.estimator.model.Subscription;
import com.estimator.repository.SubscriptionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@WebMvcTest(controllers = AdminSubscriptionController.class)
@ActiveProfiles("test")
public class AdminSubscriptionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SubscriptionRepository subscriptionRepository;

    @InjectMocks
    private AdminSubscriptionController adminSubscriptionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(adminSubscriptionController).build();
    }

    @Test
    public void testManageSubscriptions() throws Exception {
        when(subscriptionRepository.findAll()).thenReturn(List.of(new Subscription()));

        mockMvc.perform(get("/admin/subscriptions"))
                .andExpect(status().isOk())
                .andExpect(view().name("manage_subscriptions"))
                .andExpect(model().attributeExists("subscriptions"));
    }

    @Test
    public void testDeleteSubscription() throws Exception {
        when(subscriptionRepository.existsById(anyLong())).thenReturn(true);

        mockMvc.perform(post("/admin/subscriptions/delete/1").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/admin/subscriptions"));

        verify(subscriptionRepository, times(1)).deleteById(anyLong());
    }
}
