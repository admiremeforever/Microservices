package com.javabrains.resources;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.javabrains.model.CatlogItem;
import com.javabrains.model.Movie;
import com.javabrains.model.Rating;
import com.javabrains.model.UserRating;

@RestController
@RequestMapping("/catlog")
public class MovieCatlogResource {

	@Autowired
	private RestTemplate restTemplate;

	@RequestMapping("/{userId}")
	public List<CatlogItem> getCatlog(@PathVariable("userId") String userId) {
		
		UserRating ratings = restTemplate.getForObject("http://RATING-DATA-SERVICE/ratingsdata/users/" + userId,
				UserRating.class);

		return ratings.getUserRating().stream().map(rating -> {
			// For each movie ID, call movie info service and get details
			Movie movie = restTemplate.getForObject("http://MOVIE-INFO-SERVICE/movies/" + rating.getMovieID(), Movie.class);
			// put them all together
			return new CatlogItem(movie.getName(), "Desc", rating.getRating());
		})

				.collect(Collectors.toList());

	

		

	}
}
