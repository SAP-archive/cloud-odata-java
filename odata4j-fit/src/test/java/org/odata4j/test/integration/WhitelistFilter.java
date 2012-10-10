package org.odata4j.test.integration;

import java.io.IOException;
import java.util.Set;

import org.core4j.Enumerable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;

public class WhitelistFilter extends Filter {

  private static final Logger LOGGER = LoggerFactory.getLogger(WhitelistFilter.class);

  private final Set<String> whitelist;

  public WhitelistFilter(String... allowedAddresses) {
    whitelist = Enumerable.create(allowedAddresses).toSet();
  }

  @Override
  public String description() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void doFilter(HttpExchange exchange, Chain chain) throws IOException {

    String ipaddr = exchange.getRemoteAddress().getAddress().getHostAddress();
    String path = exchange.getRequestURI().getPath();

    if (whitelist.contains(ipaddr)) {
      WhitelistFilter.LOGGER.info("allow " + ipaddr + " for" + path);
      chain.doFilter(exchange);
    } else {
      WhitelistFilter.LOGGER.info("DENY " + ipaddr + " for " + path);
      exchange.close();
    }

  }

}
