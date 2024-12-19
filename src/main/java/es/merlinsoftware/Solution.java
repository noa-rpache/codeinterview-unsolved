package es.merlinsoftware;

import es.merlinsoftware.pojo.ProductSales;
import es.merlinsoftware.pojo.ProductStock;

import java.util.*;
import java.util.stream.Collectors;

public class Solution {

    private Solution() {}

    public static List<Long> sortProductsByScores(int stockWeight, int salesWeight,
                                                  List<ProductStock> productsStockInformation,
                                                  List<ProductSales> productsSalesInformation) {

        // para trabajar mejor convertimos las listas a mapas indexados por id de producto
        Map<Long, Long> stockMapping = productsStockInformation.stream()
            .collect(Collectors.toMap(ProductStock::getProductId, ProductStock::getAvailableStock));
        Map<Long, Double> salesMapping = productsSalesInformation.stream()
            .collect(Collectors.toMap(ProductSales::getProductId, ProductSales::getSalesAmount));

        // iteramos sobre uno de los mapas y calculamos el valor ponderado que nos dará un criterio de ordenación
        // asumo que si no hay un valor de un producto que solo está en una lista, el correspondiente en la otra sería 0
        // guardo los resultados en una lista
        List<ProductScore> productScores = new ArrayList<>();
        for (Long productId : stockMapping.keySet()) {
            double stock = stockMapping.getOrDefault(productId, 0L);
            double sales = salesMapping.getOrDefault(productId, 0D);

            double score = (stock * stockWeight / 100.0) + (sales * salesWeight / 100.0);
            productScores.add(new ProductScore(productId, score));
        }

        // ordenamos tomando como criterio el valor ponderado
        productScores.sort((p1, p2) -> Double.compare(p2.getScore(), p1.getScore()));

        // devolvemos una lista con solo id's
        return productScores.stream().map(ProductScore::getProductId).toList();
    }

    //añado esta clase para simular una tupla
    private static class ProductScore {
        private final Long productId;
        private final double score;

        public ProductScore(Long productId, double score) {
            this.productId = productId;
            this.score = score;
        }

        public Long getProductId() {
            return productId;
        }

        public double getScore() {
            return score;
        }
    }
}
