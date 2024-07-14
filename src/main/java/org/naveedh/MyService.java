package org.naveedh;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class MyService {

    private StringBuilder stringBuilder = new StringBuilder();

    public String doSomething(Object msg) {
        stringBuilder.append(msg).append("-");
        System.out.println("Service is doing something."+ this.hashCode());
        return stringBuilder.toString();
    }
}
