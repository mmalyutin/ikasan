package org.ikasan.replay.dao;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrInputDocument;
import org.ikasan.replay.model.SolrReplayEvent;
import org.ikasan.spec.configuration.PlatformConfigurationService;
import org.ikasan.spec.replay.ReplayDao;
import org.ikasan.spec.replay.ReplayEvent;
import org.ikasan.spec.serialiser.SerialiserFactory;
import org.ikasan.spec.solr.SolrDaoBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.support.DataAccessUtils;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by stewmi on 25/08/2017.
 */
public class SolrReplayDao extends SolrDaoBase implements ReplayDao
{
    private static Logger logger = LoggerFactory.getLogger(SolrReplayDao.class);

    /** handle to the serialiser factory */
    private SerialiserFactory serialiserFactory;
    private PlatformConfigurationService platformConfigurationService;

    /**
     * We need to give this dao it's context.
     */
    public static final String REPLAY = "replay";

    public SolrReplayDao()
    {

    }

    public SolrReplayDao(SerialiserFactory serialiserFactory,
                         PlatformConfigurationService platformConfigurationService)
    {
        this.serialiserFactory = serialiserFactory;
        if(this.serialiserFactory == null)
        {
            throw new IllegalArgumentException("serialiserFactory cannot be null!");
        }
        this.platformConfigurationService = platformConfigurationService;
        if(this.platformConfigurationService == null)
        {
            throw new IllegalArgumentException("platformConfigurationService cannot be null!");
        }
    }

    @Override
    public void saveOrUpdate(ReplayEvent replayEvent)
    {
        long millisecondsInDay = (this.daysToKeep * TimeUnit.DAYS.toMillis(1));
        long expiry = millisecondsInDay + System.currentTimeMillis();
        SolrInputDocument document = new SolrInputDocument();
        document.addField(ID, "" + replayEvent.getId());
        document.addField(TYPE, REPLAY);
        document.addField(MODULE_NAME, replayEvent.getModuleName());
        document.addField(FLOW_NAME, replayEvent.getFlowName());
        document.addField(EVENT, replayEvent.getEventId());
        if(replayEvent.getEventAsString() != null && !replayEvent.getEventAsString().isEmpty())
        {
            document.addField(PAYLOAD_CONTENT, replayEvent.getEventAsString());
        }
        else
        {
            document.addField(PAYLOAD_CONTENT, new String(replayEvent.getEvent()));
        }
        document.addField(PAYLOAD_CONTENT_RAW, replayEvent.getEvent());
        document.addField(CREATED_DATE_TIME, replayEvent.getTimestamp());
        document.setField(EXPIRY, expiry);

        try
        {
            logger.debug("Adding document: " + document);
            solrClient.add(document);
            solrClient.commit();
        }
        catch (Exception e)
        {
            throw new RuntimeException("An exception has occurred attempting to write an exclusion to Solr", e);
        }
    }

    @Override
    public List<ReplayEvent> getReplayEvents(String moduleName, String flowName, Date startDate, Date endDate)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<ReplayEvent> getReplayEvents(List<String> moduleNames, List<String> flowNames, String eventId, String payloadContent, Date fromDate, Date toDate)
    {
        List<SolrReplayEvent> results = null;

        Set<String> moduleNamesSet = null;

        if(moduleNames == null)
        {
            moduleNamesSet = new HashSet<String>();
        }
        else
        {
            moduleNamesSet = new HashSet<String>(moduleNames);
        }

        Set<String> flowNamesSet = null;

        if(flowNames == null)
        {
            flowNamesSet = new HashSet<String>();
        }
        else
        {
            flowNamesSet = new HashSet<String>(flowNames);
        }

        String queryString = this.buildQuery(moduleNamesSet, flowNamesSet, null, fromDate, toDate, payloadContent, eventId, REPLAY);

        logger.info("queryString: " + queryString);

        SolrQuery query = new SolrQuery();
        query.setQuery(queryString);
        query.setRows(100);
        query.setSort(CREATED_DATE_TIME, SolrQuery.ORDER.desc);
        query.setFields(ID, MODULE_NAME, FLOW_NAME, COMPONENT_NAME, CREATED_DATE_TIME, EVENT, PAYLOAD_CONTENT, PAYLOAD_CONTENT_RAW);

        logger.info("query: " + query.toString());

        try
        {
            QueryResponse rsp = this.solrClient.query(query);

            results = rsp.getBeans(SolrReplayEvent.class);
        }
        catch (Exception e)
        {
            throw new RuntimeException("Exception performing solr query: " + query, e);
        }

        return new ArrayList<ReplayEvent>(results);
    }

    @Override
    public void housekeep(Integer numToHousekeep)
    {
        super.removeExpired(REPLAY);
    }

    @Override
    public List<ReplayEvent> getHarvestableRecords(int housekeepingBatchSize)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public ReplayEvent getReplayEventById(Long id)
    {
        String queryString = super.buildIdQuery(id, REPLAY);

        logger.info("queryString: " + queryString);

        SolrQuery query = new SolrQuery();
        query.setQuery(queryString);
        query.setRows(100);
        query.setSort(CREATED_DATE_TIME, SolrQuery.ORDER.desc);
        query.setFields(ID, MODULE_NAME, FLOW_NAME, COMPONENT_NAME, CREATED_DATE_TIME, EVENT, PAYLOAD_CONTENT, PAYLOAD_CONTENT_RAW);

        logger.info("query: " + query.toString());

        List<SolrReplayEvent> results = null;

        try
        {
            QueryResponse rsp = this.solrClient.query(query);

            results = rsp.getBeans(SolrReplayEvent.class);
        }
        catch (Exception e)
        {
            throw new RuntimeException("Exception performing solr query: " + query, e);
        }


        return (ReplayEvent)DataAccessUtils.uniqueResult(results);
    }
}