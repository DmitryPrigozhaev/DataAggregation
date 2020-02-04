package com.prigozhaev.service;

import com.prigozhaev.model.in.Source;
import com.prigozhaev.model.in.SourceData;
import com.prigozhaev.model.in.TokenData;
import com.prigozhaev.model.out.AggregatedData;
import com.prigozhaev.service.data.DataService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author dprigozhaev on 04.02.2020
 */

@RunWith(MockitoJUnitRunner.class)
public class AggregationServiceTest {

    @InjectMocks
    @Spy
    private AggregationService aggregationService;

    @Mock
    private DataService dataService;

    @Mock
    private RestTemplate restTemplate;

    @Test
    public void shouldReturnRightAggregatedData() {

        // init
        List<Source> sourceList = new ArrayList<>();

        Source source = new Source();
        source.setId(1L);
        source.setSourceDataUrl("testSourceDataUrl");
        source.setTokenDataUrl("testTokenDataUrl");

        sourceList.add(source);

        SourceData sourceData = new SourceData();
        sourceData.setUrlType("testUrlType");
        sourceData.setVideoUrl("testVideoUrl");

        TokenData tokenData = new TokenData();
        tokenData.setValue("testValue");
        tokenData.setTtl(1L);

        ResponseEntity<SourceData> sourceDataResponse = ResponseEntity.ok(sourceData);
        sourceDataResponse.getBody().setUrlType("testUrlType");
        sourceDataResponse.getBody().setVideoUrl("testVideoUrl");

        ResponseEntity<TokenData> tokenDataResponse = ResponseEntity.ok(tokenData);
        tokenDataResponse.getBody().setValue("testValue");
        tokenDataResponse.getBody().setTtl(1L);

        // when
        Mockito.when(restTemplate.getForEntity(source.getSourceDataUrl(), SourceData.class)).thenReturn(sourceDataResponse);
        Mockito.when(restTemplate.getForEntity(source.getTokenDataUrl(), TokenData.class)).thenReturn(tokenDataResponse);

        Mockito.when(dataService.getSource()).thenReturn(sourceList);

        List<AggregatedData> result = aggregationService.getAggregatedData();

        // then
        Assert.assertNotNull(result);
        Assert.assertEquals(1, result.size());
        Assert.assertEquals(source.getId(), result.get(0).getId());
        Assert.assertEquals(sourceData.getUrlType(), result.get(0).getUrlType());
        Assert.assertEquals(sourceData.getVideoUrl(), result.get(0).getVideoUrl());
        Assert.assertEquals(tokenData.getTtl(), result.get(0).getTtl());
        Assert.assertEquals(tokenData.getValue(), result.get(0).getValue());
    }

    @Test
    public void shouldReturnEmptyListWhenDataSourceIsEmpty() {

        // init
        List<Source> sourceList = Collections.emptyList();

        // when
        Mockito.when(dataService.getSource()).thenReturn(sourceList);

        List<AggregatedData> result = aggregationService.getAggregatedData();

        // then
        Assert.assertNotNull(result);
        Assert.assertEquals(0, result.size());
    }

}