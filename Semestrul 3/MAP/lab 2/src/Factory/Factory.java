package Factory;

import Containers.Container;

public interface Factory {
    Container createContainer(Strategy strategy);
}