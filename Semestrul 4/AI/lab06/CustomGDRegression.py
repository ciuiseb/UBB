import random
import numpy as np

class CustomGDRegressor:
    def __init__(self):
        self.intercept_ = 0.0
        self.coef_ = []

    def fit(self, X, y, learning_rate=0.001, n_epochs=1000, batch_size=None, random_init=False):
        n_samples = len(X)
        n_features = len(X[0])

        if random_init:
            self.coef_ = [random.random() for _ in range(n_features + 1)]
        else:
            self.coef_ = [0.0 for _ in range(n_features + 1)]

        if batch_size is None or batch_size > n_samples:
            batch_size = n_samples

        for epoch in range(n_epochs):
            indices = list(range(n_samples))
            random.shuffle(indices)

            for start_idx in range(0, n_samples, batch_size):
                batch_indices = indices[start_idx:min(start_idx + batch_size, n_samples)]

                grad_coef = [0.0] * (n_features + 1)

                for i in batch_indices:
                    y_computed = self.predict_single(X[i])
                    error = y_computed - y[i]

                    for j in range(n_features):
                        grad_coef[j] += error * X[i][j]

                    grad_coef[n_features] += error

                batch_size_actual = len(batch_indices)
                for j in range(n_features + 1):
                    grad_coef[j] /= batch_size_actual
                    self.coef_[j] = self.coef_[j] - learning_rate * grad_coef[j]

        self.intercept_ = self.coef_[-1]
        self.coef_ = self.coef_[:-1]

    def predict_single(self, xi):
        y_pred = self.coef_[-1]
        for j in range(len(xi)):
            y_pred += self.coef_[j] * xi[j]
        return y_pred

    def predict(self, X):
        return [self.predict_single(xi) for xi in X]