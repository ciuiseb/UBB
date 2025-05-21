import numpy as np

class SgdAnn:
    def __init__(self, hidden_layer_sizes=(100,),
                 learning_rate_init=0.001, max_iter=200,
                 random_state=None, verbose=False,
                 early_stopping=False, tol=1e-4):

        self.hidden_layer_sizes = hidden_layer_sizes
        self.learning_rate = learning_rate_init
        self.max_iter = max_iter
        self.verbose = verbose
        self.tol = tol
        self.early_stopping = early_stopping
        self.loss_history = []

        if random_state is not None:
            np.random.seed(random_state)

        self.weights = []
        self.biases = []
        self.is_initialized = False

    def _initialize_parameters(self, input_size, output_size):
        layer_sizes = [input_size] + list(self.hidden_layer_sizes) + [output_size]

        self.weights = []
        self.biases = []
        for i in range(1, len(layer_sizes)):
            scale = np.sqrt(2.0 / (layer_sizes[i-1] + layer_sizes[i]))
            self.weights.append(np.random.randn(layer_sizes[i-1], layer_sizes[i]) * scale)
            self.biases.append(np.zeros((1, layer_sizes[i])))

        self.is_initialized = True

    def activation(self, x):
        return np.maximum(0, x)

    def activation_derivative(self, x):
        return np.where(x > 0, 1, 0)


    def forward(self, X):
        activations = [X]
        pre_activations = []

        for i in range(len(self.weights) - 1):
            z = np.dot(activations[-1], self.weights[i]) + self.biases[i]
            pre_activations.append(z)
            a = self.activation(z)
            activations.append(a)

        z = np.dot(activations[-1], self.weights[-1]) + self.biases[-1]
        pre_activations.append(z)

        exp_z = np.exp(z - np.max(z, axis=1, keepdims=True))
        a = exp_z / np.sum(exp_z, axis=1, keepdims=True)
        activations.append(a)

        return activations, pre_activations

    def fit(self, X, y):
        input_size = X.shape[1]
        if len(np.unique(y)) > 2:
            output_size = len(np.unique(y))
        else:
            output_size = 2

        if not self.is_initialized:
            self._initialize_parameters(input_size, output_size)

        m = X.shape[0]

        if output_size > 1:
            y_one_hot = np.zeros((m, output_size))
            y_one_hot[np.arange(m), y.astype(int)] = 1
            y = y_one_hot
        else:
            y = y.reshape(-1, 1)

        prev_loss = float('inf')
        for iteration in range(self.max_iter):
            activations, pre_activations = self.forward(X)

            loss = -np.mean(np.sum(y * np.log(activations[-1] + 1e-15), axis=1))
            self.loss_history.append(loss)

            if self.early_stopping and abs(prev_loss - loss) < self.tol:
                if self.verbose:
                    print(f"Converged at iteration {iteration}, Loss = {loss:.8f}")
                break

            prev_loss = loss

            if self.verbose and iteration % 10 == 0:
                print(f"Iteration {iteration}, Loss = {loss:.8f}")

            delta = activations[-1] - y
            dWeights = []
            dBiases = []

            for l in reversed(range(len(self.weights))):
                dw = np.dot(activations[l].T, delta) / m
                db = np.sum(delta, axis=0, keepdims=True) / m

                dWeights.insert(0, dw)
                dBiases.insert(0, db)

                if l > 0:
                    delta = np.dot(delta, self.weights[l].T) * self.activation_derivative(pre_activations[l-1])

            for l in range(len(self.weights)):
                self.weights[l] -= self.learning_rate * dWeights[l]
                self.biases[l] -= self.learning_rate * dBiases[l]

        return self

    def predict_proba(self, X):
        activations, _ = self.forward(X)
        return activations[-1]

    def predict(self, X):
        probs = self.predict_proba(X)
        if probs.shape[1] == 1:
            return (probs > 0.5).astype(int).flatten()
        else:
            return np.argmax(probs, axis=1)