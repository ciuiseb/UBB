//package triathlon.network.utils;
//
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import triathlon.services.ITriathlonServices;
//
//import java.net.Socket;
//
//public class TriathlonJsonConcurentServer extends AbsConcurrentServer {
//    private static Logger logger = LogManager.getLogger(TriathlonJsonConcurentServer.class);
//    private ITriathlonServices server;
//
//    public TriathlonJsonConcurentServer(int port, ITriathlonServices ITriathlonServices) {
//        super(port);
//        this.server = ITriathlonServices;
//        logger.debug("Concurrent Json Server");
//    }
//
//    @Override
//    protected Thread createWorker(Socket client) {
////        return new Thread(new Tria(client, worker));
//        return null;
//    }
//}
