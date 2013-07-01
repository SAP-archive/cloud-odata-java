package com.sap.core.odata.testutil.tool.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 
 * @author SAP AG
 */
public class SupaPerformanceTestClient extends AbstractTestClient {

  private static final String SUPA_CONFIG = "target/classes/performance/Supa-Config.properties";
  private static final Logger LOG = LoggerFactory.getLogger(SupaPerformanceTestClient.class);
  
  private final SupaController supa;
  private int runsPerTest;
  private int warmupRuns;

  public boolean supaStart;
  private boolean supaExit;
  public String supaHome;
  public String supaConfig;
  
  SupaPerformanceTestClient(final CallerConfig config, String supaBaseUrl) throws URISyntaxException {
    super(config);
    supa = new SupaController(supaBaseUrl);
  }

  /**
   * 
   * @return
   */
  public String runMeasurement() {
    if(supaStart) {
      startSupa();
    }
    
    List<TestRequest> testRequests = config.getTestRequests();

    for (TestRequest testRequest : testRequests) {
      String scenarioName = supa.getCurrentStepName();
      LOG.info("Prepare for test step: {}", scenarioName);
      // run warmup
      if(warmupRuns > 0) {
        LOG.info("Start warmup ({})", warmupRuns);
        call(testRequest, warmupRuns);
      }
      // run test
      for (int i = 1; i <= runsPerTest; i++) {
        LOG.info("Start run '{}/{}'.", i, runsPerTest);
        supa.begin();
        call(testRequest, testRequest.getCallCount());
        String result = supa.stop();
        
        LOG.info("...result: {}", result);
      }
      supa.nextStep();
    }
    
    String resultsPath = supa.finish();
    if(supaExit) {
      supa.shutdownSupaServer();
    }
    return resultsPath;
  }

  /**
   * 
   */
  private boolean startSupa() {
    try {
      File supaJarFile = new File(supaHome + "/supaStarter.jar");
      if(!supaJarFile.exists()) {
        throw new IllegalArgumentException("No supaStarter.jar found at " + supaJarFile.getAbsolutePath());
      }
      File supaConfigFile = new File(supaConfig);
      if(!supaConfigFile.exists()) {
        throw new IllegalArgumentException("No configuration for supa found at " + supaConfigFile.getAbsolutePath());
      }

      String execCommand = "java -jar " + supaJarFile.getAbsolutePath() + " " + supaConfigFile.getAbsolutePath() + " -server";
      Process supaServer = Runtime.getRuntime().exec(execCommand, null, new File("/tmp"));
      InputStream in = supaServer.getInputStream();
      BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
      
      String read = reader.readLine();
      LOG.info(read);
      if(read.contains("Hit Return key to close window ...")) {
        supaServer.destroy();
        LOG.info("SUPA Failure...");
        return false;
      } else if(read.contains("Startup")) {
        LOG.info("SUPA Started...");
        return true;
      } else {
        throw new RuntimeException("Unknown output: " + read);
      }
    } catch (IOException e) {
      throw new RuntimeException("Exception occured: " + e.getMessage(), e);
    }
  }

  
  
  //
  // Builder below
  // 
  
  public static SupaPerformanceTestClientBuilder create(final CallerConfig config, String supaBaseUrl) throws URISyntaxException {
    return new SupaPerformanceTestClientBuilder(config, supaBaseUrl);
  }
  
  public static class SupaPerformanceTestClientBuilder {
    private final SupaPerformanceTestClient client;
    
    public SupaPerformanceTestClientBuilder(CallerConfig config, String supaBaseUrl) throws URISyntaxException {
      client = new SupaPerformanceTestClient(config, supaBaseUrl);
      // set default values
      client.runsPerTest = 3;
      client.warmupRuns = -1;
      client.supaExit = false;
      client.supaStart = false;
      client.supaConfig = SUPA_CONFIG;
    }
    
    public SupaPerformanceTestClientBuilder runsPerTest(int runsPerTest) {
      client.runsPerTest = runsPerTest;
      return this;
    }
    
    public SupaPerformanceTestClientBuilder warmupRuns(int warmupRuns) {
      client.warmupRuns = warmupRuns;
      return this;
    }
    
    public SupaPerformanceTestClient build() {
      return client;
    }

    public SupaPerformanceTestClientBuilder startSupa(String supaHome) {
      return this.startSupa(supaHome, SUPA_CONFIG);
    }
    
    public SupaPerformanceTestClientBuilder startSupa(String supaHome, String supaConfig) {
      client.supaStart = true;
      client.supaHome = supaHome;
      client.supaConfig = supaConfig;
      return this;
    }

    public SupaPerformanceTestClientBuilder exitSupa() {
      client.supaExit = true;
      return this;
    }
  }
}
