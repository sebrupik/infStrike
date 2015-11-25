package infStrike.objects;

/**
* A request can be sent by anyone and request anything. 
* It is currently intended to be sent by 
* Platoons asking for :
*	missions
*	support
* Bases asking for :
*	defence
*
* The content of the request can be anything. How it is itterpreted depends
* on the AIController it is sent to.
* 
*/


public class AIRequest {

private Object requester;
private String request;
private double priority; //0.0 (low) - 1.0 (high)

    public AIRequest(Object requester, String request, double priority) {
        this.requester = requester;
        this.request = request;
        this.priority = priority;
    }

    public Object getRequester() { return requester; }
    public String getRequest() { return request; }
    public double getPriority() { return priority; }
}