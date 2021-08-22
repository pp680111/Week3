package server.filter;

import io.netty.channel.CombinedChannelDuplexHandler;

public class HttpFilterAdapter extends CombinedChannelDuplexHandler<HttpRequestFilterHandler, HttpResponseFilterHandler> {

}
