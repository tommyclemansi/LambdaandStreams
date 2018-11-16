package org.paumard.lambdamasterclass.part1.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public interface CurrencyConverter {

    double convert(double amount);

	@FunctionalInterface
	interface BiFunction {

		// this is what defines our Bifunction, the implementation is defined in the TriFunction
		Double convert(Double amount, String to);

		// this is the final return statement, it returns a CurrencyConverter, that is converting using
		default CurrencyConverter to(String toCurrency) {
			return (amount) -> {
				System.out.println("returning in Bifunction");
				return this.convert(amount, toCurrency);
			};
		}
	}

	// this is the declaration of the famous Trifunctino that we implemented below
	@FunctionalInterface
	interface TriFunction {

		// this is what defines our Trifunction, the implementation is defined in CurrencyConverter
		Double convertInTrifunction(Double amount, String from, String to);

		// TriFunction is returned by the of static method, so we need to create a from method here..
		// what we do here is partial application
		default BiFunction from(String fromCurrency) {
			// we return the BiFunction here.. we need to define it above.. (so the implementation)
			return (amount, to) -> {
				System.out.println("returning in trifunction");
				return this.convertInTrifunction(amount, fromCurrency, to);
			};
		}
	}

	// CurrencyConverter converter = CurrencyConverter.of(date).from("EUR").to("GBP");
	static TriFunction of(LocalDate date) // ok we take one Argument
	{
		// we will return a lambda, that is actually is a TriFunction (see above, we needed to create it)
		// te first return is the return of the lambda
		// we have 3 parameters that are unknown at this point
		return (amount, from, to) -> {
			Path path = Paths.get("data/currency.txt");
			try (Stream<String> lines = Files.lines(path)) {
				Map<String, Double> convertMap = lines.skip(1L).collect(Collectors.toMap(line -> line.substring(0, 3), // key
						line -> Double.parseDouble(line.substring(4))    // value
				));
				return amount * convertMap.get(to) / convertMap.get(from);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		};

	}

}
