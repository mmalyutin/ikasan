package org.ikasan.replay.dao;

import org.apache.solr.SolrTestCaseJ4;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.client.solrj.request.CoreAdminRequest;
import org.apache.solr.client.solrj.request.UpdateRequest;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.core.NodeConfig;
import org.apache.solr.core.SolrResourceLoader;
import org.ikasan.replay.model.SolrReplayEvent;
import org.ikasan.spec.configuration.PlatformConfigurationService;
import org.ikasan.spec.replay.ReplayEvent;
import org.ikasan.spec.serialiser.SerialiserFactory;
import org.ikasan.wiretap.dao.SolrWiretapDao;
import org.ikasan.wiretap.model.SolrWiretapEvent;
import org.ikasan.wiretap.service.SolrWiretapServiceImpl;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;
import org.springframework.test.annotation.DirtiesContext;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by Ikasan Development on 29/08/2017.
 */
public class SolrReplayDaoTest extends SolrTestCaseJ4
{
    /**
     * Mockery for mocking concrete classes
     */
    private Mockery mockery = new Mockery()
    {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };

    private SolrClient server = mockery.mock(SolrClient.class);


    @Test
    @DirtiesContext
    public void test_query_replay_events() throws Exception
    {
        Path path = createTempDir();

        SolrResourceLoader loader = new SolrResourceLoader(path);
        NodeConfig config = new NodeConfig.NodeConfigBuilder("testnode", loader)
                .setConfigSetBaseDirectory(Paths.get(TEST_HOME()).resolve("configsets").toString())
                .build();

        try (EmbeddedSolrServer server = new EmbeddedSolrServer(config, "ikasan"))
        {
            CoreAdminRequest.Create createRequest = new CoreAdminRequest.Create();
            createRequest.setCoreName("ikasan");
            createRequest.setConfigSet("minimal");
            server.request(createRequest);


            SolrReplayDao dao = new SolrReplayDao();
            dao.setSolrClient(server);

            SolrReplayEvent replayEvent = new SolrReplayEvent();
            replayEvent.setModuleName("moduleName");
            replayEvent.setFlowName("flowName");
            replayEvent.setEvent("event".getBytes());
            replayEvent.setTimestamp(System.currentTimeMillis());
            replayEvent.setExpiry(0);
            replayEvent.setId(1l);

            dao.saveOrUpdate(replayEvent);

            List<ReplayEvent> replayEventList = dao.getReplayEvents(new ArrayList<>(), new ArrayList<>()
                , null,null, new Date(System.currentTimeMillis() - 10000000l), new Date(System.currentTimeMillis() + 10000000l), 10);

            assertEquals(2, replayEventList.size());

            server.close();
        }
    }

    @Test
    @DirtiesContext
    public void test_query_replay_events_null_module_names_and_flow_names() throws Exception
    {
        Path path = createTempDir();

        SolrResourceLoader loader = new SolrResourceLoader(path);
        NodeConfig config = new NodeConfig.NodeConfigBuilder("testnode", loader)
                .setConfigSetBaseDirectory(Paths.get(TEST_HOME()).resolve("configsets").toString())
                .build();

        try (EmbeddedSolrServer server = new EmbeddedSolrServer(config, "ikasan"))
        {
            CoreAdminRequest.Create createRequest = new CoreAdminRequest.Create();
            createRequest.setCoreName("ikasan");
            createRequest.setConfigSet("minimal");
            server.request(createRequest);


            SolrReplayDao dao = new SolrReplayDao();
            dao.setSolrClient(server);

            SolrReplayEvent replayEvent = new SolrReplayEvent();
            replayEvent.setModuleName("moduleName");
            replayEvent.setFlowName("flowName");
            replayEvent.setEvent("event".getBytes());
            replayEvent.setTimestamp(System.currentTimeMillis());
            replayEvent.setExpiry(0);
            replayEvent.setId(1l);

            dao.saveOrUpdate(replayEvent);

            List<ReplayEvent> replayEventList = dao.getReplayEvents(null, null
                    , null,null, new Date(System.currentTimeMillis() - 10000000l), new Date(System.currentTimeMillis() + 10000000l), 10);

            assertEquals(2, replayEventList.size());

            server.close();
        }
    }

    @Test
    @DirtiesContext
    public void test_query_replay_events_with_module_name() throws Exception
    {
        Path path = createTempDir();

        SolrResourceLoader loader = new SolrResourceLoader(path);
        NodeConfig config = new NodeConfig.NodeConfigBuilder("testnode", loader)
                .setConfigSetBaseDirectory(Paths.get(TEST_HOME()).resolve("configsets").toString())
                .build();

        try (EmbeddedSolrServer server = new EmbeddedSolrServer(config, "ikasan"))
        {
            CoreAdminRequest.Create createRequest = new CoreAdminRequest.Create();
            createRequest.setCoreName("ikasan");
            createRequest.setConfigSet("minimal");
            server.request(createRequest);


            SolrReplayDao dao = new SolrReplayDao();
            dao.setSolrClient(server);

            SolrReplayEvent replayEvent = new SolrReplayEvent();
            replayEvent.setModuleName("moduleName");
            replayEvent.setFlowName("flowName");
            replayEvent.setEvent("event".getBytes());
            replayEvent.setTimestamp(System.currentTimeMillis());
            replayEvent.setExpiry(0);
            replayEvent.setId(1l);

            dao.saveOrUpdate(replayEvent);

            ArrayList moduleNames = new ArrayList<>();
            moduleNames.add("moduleName");

            List<ReplayEvent> replayEventList = dao.getReplayEvents(moduleNames, new ArrayList<>()
                    , null,null, new Date(System.currentTimeMillis() - 10000000l), new Date(System.currentTimeMillis() + 10000000l), 10);

            assertEquals(2, replayEventList.size());

            server.close();
        }
    }

    @Test
    @DirtiesContext
    public void test_query_replay_events_with_flow_name() throws Exception
    {
        Path path = createTempDir();

        SolrResourceLoader loader = new SolrResourceLoader(path);
        NodeConfig config = new NodeConfig.NodeConfigBuilder("testnode", loader)
                .setConfigSetBaseDirectory(Paths.get(TEST_HOME()).resolve("configsets").toString())
                .build();

        try (EmbeddedSolrServer server = new EmbeddedSolrServer(config, "ikasan"))
        {
            CoreAdminRequest.Create createRequest = new CoreAdminRequest.Create();
            createRequest.setCoreName("ikasan");
            createRequest.setConfigSet("minimal");
            server.request(createRequest);


            SolrReplayDao dao = new SolrReplayDao();
            dao.setSolrClient(server);

            SolrReplayEvent replayEvent = new SolrReplayEvent();
            replayEvent.setModuleName("moduleName");
            replayEvent.setFlowName("flowName");
            replayEvent.setEvent("event".getBytes());
            replayEvent.setTimestamp(System.currentTimeMillis());
            replayEvent.setExpiry(0);
            replayEvent.setId(1l);

            dao.saveOrUpdate(replayEvent);

            ArrayList flowNames = new ArrayList<>();
            flowNames.add("flowName");

            List<ReplayEvent> replayEventList = dao.getReplayEvents(new ArrayList<>(), flowNames
                    , null,null, new Date(System.currentTimeMillis() - 10000000l), new Date(System.currentTimeMillis() + 10000000l), 10);

            assertEquals(2, replayEventList.size());

            server.close();
        }
    }

    @Test
    @DirtiesContext
    public void test_restrict_result_size() throws Exception
    {
        Path path = createTempDir();

        SolrResourceLoader loader = new SolrResourceLoader(path);
        NodeConfig config = new NodeConfig.NodeConfigBuilder("testnode", loader)
            .setConfigSetBaseDirectory(Paths.get(TEST_HOME()).resolve("configsets").toString())
            .build();

        try (EmbeddedSolrServer server = new EmbeddedSolrServer(config, "ikasan"))
        {
            CoreAdminRequest.Create createRequest = new CoreAdminRequest.Create();
            createRequest.setCoreName("ikasan");
            createRequest.setConfigSet("minimal");
            server.request(createRequest);


            SolrReplayDao dao = new SolrReplayDao();
            dao.setSolrClient(server);

            SolrReplayEvent replayEvent = new SolrReplayEvent();
            replayEvent.setModuleName("moduleName");
            replayEvent.setFlowName("flowName");
            replayEvent.setEvent("event".getBytes());
            replayEvent.setTimestamp(System.currentTimeMillis());
            replayEvent.setExpiry(0);
            replayEvent.setId(1l);

            dao.saveOrUpdate(replayEvent);

            ArrayList flowNames = new ArrayList<>();
            flowNames.add("flowName");

            List<ReplayEvent> replayEventList = dao.getReplayEvents(new ArrayList<>(), flowNames
                , null,null, new Date(System.currentTimeMillis() - 10000000l), new Date(System.currentTimeMillis() + 10000000l), 1);

            assertEquals(1, replayEventList.size());

            server.close();
        }
    }

    @Test
    @DirtiesContext
    public void test_query_replay_events_with_payload_content() throws Exception
    {
        Path path = createTempDir();

        SolrResourceLoader loader = new SolrResourceLoader(path);
        NodeConfig config = new NodeConfig.NodeConfigBuilder("testnode", loader)
                .setConfigSetBaseDirectory(Paths.get(TEST_HOME()).resolve("configsets").toString())
                .build();

        try (EmbeddedSolrServer server = new EmbeddedSolrServer(config, "ikasan"))
        {
            CoreAdminRequest.Create createRequest = new CoreAdminRequest.Create();
            createRequest.setCoreName("ikasan");
            createRequest.setConfigSet("minimal");
            server.request(createRequest);


            SolrReplayDao dao = new SolrReplayDao();
            dao.setSolrClient(server);

            SolrReplayEvent replayEvent = new SolrReplayEvent();
            replayEvent.setModuleName("moduleName");
            replayEvent.setFlowName("flowName");
            replayEvent.setEvent("event".getBytes());
            replayEvent.setTimestamp(System.currentTimeMillis());
            replayEvent.setExpiry(0);
            replayEvent.setId(1l);

            dao.saveOrUpdate(replayEvent);

            replayEvent = new SolrReplayEvent();
            replayEvent.setModuleName("moduleName");
            replayEvent.setFlowName("flowName");
            replayEvent.setEvent("data".getBytes());
            replayEvent.setTimestamp(System.currentTimeMillis());
            replayEvent.setExpiry(0);
            replayEvent.setId(2l);

            dao.saveOrUpdate(replayEvent);

            ArrayList flowNames = new ArrayList<>();
            flowNames.add("flowName");

            List<ReplayEvent> replayEventList = dao.getReplayEvents(new ArrayList<>(), flowNames
                    , null,"data", new Date(System.currentTimeMillis() - 10000000l), new Date(System.currentTimeMillis() + 10000000l), 10);

            assertEquals(2, replayEventList.size());

            server.close();
        }
    }

    @Test
    @DirtiesContext
    public void test_query_replay_events_with_event_id() throws Exception
    {
        Path path = createTempDir();

        SolrResourceLoader loader = new SolrResourceLoader(path);
        NodeConfig config = new NodeConfig.NodeConfigBuilder("testnode", loader)
                .setConfigSetBaseDirectory(Paths.get(TEST_HOME()).resolve("configsets").toString())
                .build();

        try (EmbeddedSolrServer server = new EmbeddedSolrServer(config, "ikasan"))
        {
            CoreAdminRequest.Create createRequest = new CoreAdminRequest.Create();
            createRequest.setCoreName("ikasan");
            createRequest.setConfigSet("minimal");
            server.request(createRequest);


            SolrReplayDao dao = new SolrReplayDao();
            dao.setSolrClient(server);

            SolrReplayEvent replayEvent = new SolrReplayEvent();
            replayEvent.setModuleName("moduleName");
            replayEvent.setFlowName("flowName");
            replayEvent.setEvent("event".getBytes());
            replayEvent.setTimestamp(System.currentTimeMillis());
            replayEvent.setExpiry(0);
            replayEvent.setEventId("eventId1");
            replayEvent.setId(1l);

            dao.saveOrUpdate(replayEvent);

            replayEvent = new SolrReplayEvent();
            replayEvent.setModuleName("moduleName");
            replayEvent.setFlowName("flowName");
            replayEvent.setEvent("data".getBytes());
            replayEvent.setTimestamp(System.currentTimeMillis());
            replayEvent.setExpiry(0);
            replayEvent.setEventId("eventId2");
            replayEvent.setId(2l);

            dao.saveOrUpdate(replayEvent);

            ArrayList flowNames = new ArrayList<>();
            flowNames.add("flowName");

            List<ReplayEvent> replayEventList = dao.getReplayEvents(new ArrayList<>(), flowNames
                    , "eventId2","data", new Date(System.currentTimeMillis() - 10000000l), new Date(System.currentTimeMillis() + 10000000l), 10);

            assertEquals(2, replayEventList.size());

            replayEventList = dao.getReplayEvents(new ArrayList<>(), flowNames
                    , "eventId1","data", new Date(System.currentTimeMillis() - 10000000l), new Date(System.currentTimeMillis() + 10000000l), 10);

            assertEquals(0, replayEventList.size());

            server.close();
        }
    }

    @Test
    @DirtiesContext
    public void test_query_replay_events_event_as_string() throws Exception
    {
        Path path = createTempDir();

        SolrResourceLoader loader = new SolrResourceLoader(path);
        NodeConfig config = new NodeConfig.NodeConfigBuilder("testnode", loader)
                .setConfigSetBaseDirectory(Paths.get(TEST_HOME()).resolve("configsets").toString())
                .build();

        try (EmbeddedSolrServer server = new EmbeddedSolrServer(config, "ikasan"))
        {
            CoreAdminRequest.Create createRequest = new CoreAdminRequest.Create();
            createRequest.setCoreName("ikasan");
            createRequest.setConfigSet("minimal");
            server.request(createRequest);


            SolrReplayDao dao = new SolrReplayDao();
            dao.setSolrClient(server);

            SolrReplayEvent replayEvent = new SolrReplayEvent();
            replayEvent.setModuleName("moduleName");
            replayEvent.setFlowName("flowName");
            replayEvent.setEventAsString("event");
            replayEvent.setEvent("event".getBytes());
            replayEvent.setTimestamp(System.currentTimeMillis());
            replayEvent.setExpiry(0);
            replayEvent.setId(1l);

            dao.saveOrUpdate(replayEvent);

            List<ReplayEvent> replayEventList = dao.getReplayEvents(new ArrayList<>(), new ArrayList<>()
                    , null,null, new Date(System.currentTimeMillis() - 10000000l), new Date(System.currentTimeMillis() + 10000000l), 10);

            assertEquals(2, replayEventList.size());

            server.close();
        }
    }

    @Test
    @DirtiesContext
    public void test_query_replay_events_find_by_id() throws Exception
    {
        Path path = createTempDir();

        SolrResourceLoader loader = new SolrResourceLoader(path);
        NodeConfig config = new NodeConfig.NodeConfigBuilder("testnode", loader)
                .setConfigSetBaseDirectory(Paths.get(TEST_HOME()).resolve("configsets").toString())
                .build();

        try (EmbeddedSolrServer server = new EmbeddedSolrServer(config, "ikasan"))
        {
            CoreAdminRequest.Create createRequest = new CoreAdminRequest.Create();
            createRequest.setCoreName("ikasan");
            createRequest.setConfigSet("minimal");
            server.request(createRequest);


            SolrReplayDao dao = new SolrReplayDao();
            dao.setSolrClient(server);

            SolrReplayEvent replayEvent = new SolrReplayEvent();
            replayEvent.setModuleName("moduleName");
            replayEvent.setFlowName("flowName");
            replayEvent.setEventAsString("event");
            replayEvent.setEvent("event".getBytes());
            replayEvent.setTimestamp(System.currentTimeMillis());
            replayEvent.setExpiry(0);
            replayEvent.setId(1l);

            dao.saveOrUpdate(replayEvent);

            ReplayEvent replayEvent1 = dao.getReplayEventById(1l);

            assertNotNull(replayEvent1);

            server.close();
        }
    }

    @Test
    @DirtiesContext
    public void test_query_replay_events_find_by_id_null_result() throws Exception
    {
        Path path = createTempDir();

        SolrResourceLoader loader = new SolrResourceLoader(path);
        NodeConfig config = new NodeConfig.NodeConfigBuilder("testnode", loader)
                .setConfigSetBaseDirectory(Paths.get(TEST_HOME()).resolve("configsets").toString())
                .build();

        try (EmbeddedSolrServer server = new EmbeddedSolrServer(config, "ikasan"))
        {
            CoreAdminRequest.Create createRequest = new CoreAdminRequest.Create();
            createRequest.setCoreName("ikasan");
            createRequest.setConfigSet("minimal");
            server.request(createRequest);


            SolrReplayDao dao = new SolrReplayDao();
            dao.setSolrClient(server);

            SolrReplayEvent replayEvent = new SolrReplayEvent();
            replayEvent.setModuleName("moduleName");
            replayEvent.setFlowName("flowName");
            replayEvent.setEventAsString("event");
            replayEvent.setEvent("event".getBytes());
            replayEvent.setTimestamp(System.currentTimeMillis());
            replayEvent.setExpiry(0);
            replayEvent.setId(1l);

            dao.saveOrUpdate(replayEvent);

            ReplayEvent replayEvent1 = dao.getReplayEventById(2l);

            assertNull(replayEvent1);

            server.close();
        }
    }

    @Test
    @DirtiesContext
    public void test_query_replay_events_event_as_string_empty() throws Exception
    {
        Path path = createTempDir();

        SolrResourceLoader loader = new SolrResourceLoader(path);
        NodeConfig config = new NodeConfig.NodeConfigBuilder("testnode", loader)
                .setConfigSetBaseDirectory(Paths.get(TEST_HOME()).resolve("configsets").toString())
                .build();

        try (EmbeddedSolrServer server = new EmbeddedSolrServer(config, "ikasan"))
        {
            CoreAdminRequest.Create createRequest = new CoreAdminRequest.Create();
            createRequest.setCoreName("ikasan");
            createRequest.setConfigSet("minimal");
            server.request(createRequest);


            SolrReplayDao dao = new SolrReplayDao();
            dao.setSolrClient(server);

            SolrReplayEvent replayEvent = new SolrReplayEvent();
            replayEvent.setModuleName("moduleName");
            replayEvent.setFlowName("flowName");
            replayEvent.setEventAsString("");
            replayEvent.setEvent("event".getBytes());
            replayEvent.setTimestamp(System.currentTimeMillis());
            replayEvent.setExpiry(0);
            replayEvent.setId(1l);

            dao.saveOrUpdate(replayEvent);

            List<ReplayEvent> replayEventList = dao.getReplayEvents(new ArrayList<>(), new ArrayList<>()
                    , null,null, new Date(System.currentTimeMillis() - 10000000l), new Date(System.currentTimeMillis() + 10000000l), 10);

            assertEquals(2, replayEventList.size());

            server.close();
        }
    }

    @Test
    @DirtiesContext
    public void test_housekeep_success() throws Exception
    {
        Path path = createTempDir();

        SolrResourceLoader loader = new SolrResourceLoader(path);
        NodeConfig config = new NodeConfig.NodeConfigBuilder("testnode", loader)
                .setConfigSetBaseDirectory(Paths.get(TEST_HOME()).resolve("configsets").toString())
                .build();

        try (EmbeddedSolrServer server = new EmbeddedSolrServer(config, "ikasan"))
        {
            CoreAdminRequest.Create createRequest = new CoreAdminRequest.Create();
            createRequest.setCoreName("ikasan");
            createRequest.setConfigSet("minimal");
            server.request(createRequest);


            SolrReplayDao dao = new SolrReplayDao();
            dao.setSolrClient(server);
            dao.setDaysToKeep(0);

            SolrReplayEvent replayEvent = new SolrReplayEvent();
            replayEvent.setModuleName("moduleName");
            replayEvent.setFlowName("flowName");
            replayEvent.setEventAsString("");
            replayEvent.setEvent("event".getBytes());
            replayEvent.setTimestamp(System.currentTimeMillis());
            replayEvent.setExpiry(System.currentTimeMillis());
            replayEvent.setId(1l);

            dao.saveOrUpdate(replayEvent);

            List<ReplayEvent> replayEventList = dao.getReplayEvents(new ArrayList<>(), new ArrayList<>()
                    , null,null, new Date(System.currentTimeMillis() - 10000000l), new Date(System.currentTimeMillis() + 10000000l), 10);

            assertEquals(2, replayEventList.size());

            dao.housekeep(100);

            replayEventList = dao.getReplayEvents(new ArrayList<>(), new ArrayList<>()
                    , null,null, new Date(System.currentTimeMillis() - 10000000l), new Date(System.currentTimeMillis() + 10000000l), 10);

            assertEquals(0, replayEventList.size());

            server.close();
        }
    }

    @Test(expected = RuntimeException.class)
    @DirtiesContext
    public void test_save_exception() throws Exception
    {
        mockery.checking(new Expectations()
        {
            {
                // set event factory
                oneOf(server).request(with(any(UpdateRequest.class)));
                will(throwException(new RuntimeException("Error")));

            }
        });

        SolrReplayDao dao = new SolrReplayDao();
        dao.setSolrClient(server);

        SolrReplayEvent replayEvent = new SolrReplayEvent();
        replayEvent.setModuleName("moduleName");
        replayEvent.setFlowName("flowName");
        replayEvent.setEventAsString("");
        replayEvent.setEvent("event".getBytes());
        replayEvent.setTimestamp(System.currentTimeMillis());
        replayEvent.setExpiry(0);
        replayEvent.setId(1l);


        dao.saveOrUpdate(replayEvent);
    }

    @Test(expected = RuntimeException.class)
    @DirtiesContext
    public void test_find_by_id_exception() throws Exception
    {
        mockery.checking(new Expectations()
        {
            {
                // set event factory
                oneOf(server).request(with(any(SolrRequest.class)));
                will(throwException(new RuntimeException("Error")));

            }
        });

        SolrReplayDao dao = new SolrReplayDao();
        dao.setSolrClient(server);

        dao.getReplayEventById(1l);
    }

    @Test(expected = RuntimeException.class)
    @DirtiesContext
    public void test_get_replay_events_exception() throws Exception
    {
        mockery.checking(new Expectations()
        {
            {
                // set event factory
                oneOf(server).request(with(any(SolrRequest.class)));
                will(throwException(new RuntimeException("Error")));

            }
        });

        SolrReplayDao dao = new SolrReplayDao();
        dao.setSolrClient(server);

        dao.getReplayEvents(new ArrayList<>(), new ArrayList<>()
                , null,null, new Date(System.currentTimeMillis() - 10000000l), new Date(System.currentTimeMillis() + 10000000l), 10);
    }

    public static String TEST_HOME() {
        return getFile("solr/ikasan").getParent();
    }

    public static Path TEST_PATH() {
        return getFile("solr/ikasan").getParentFile().toPath();
    }
}
