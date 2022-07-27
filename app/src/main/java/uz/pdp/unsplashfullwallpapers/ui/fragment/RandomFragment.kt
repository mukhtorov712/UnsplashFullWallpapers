package uz.pdp.unsplashfullwallpapers.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import uz.pdp.unsplashfullwallpapers.R
import uz.pdp.unsplashfullwallpapers.adapters.ImageAdapter
import uz.pdp.unsplashfullwallpapers.databinding.FragmentRandomBinding
import uz.pdp.unsplashfullwallpapers.model.ImageModel
import uz.pdp.unsplashfullwallpapers.ui.viewmodel.RandomViewModel
import kotlin.coroutines.CoroutineContext

class RandomFragment : Fragment(),CoroutineScope {

    private lateinit var randomViewModel: RandomViewModel
    private var _binding: FragmentRandomBinding? = null

    private val binding get() = _binding!!
    lateinit var imageAdapter: ImageAdapter
    private val job = Job()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRandomBinding.inflate(inflater, container, false)
        randomViewModel =
            ViewModelProvider(this)[RandomViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageAdapter = ImageAdapter(object : ImageAdapter.OnImageClickListener {
            override fun onImageClick(imageModel: ImageModel) {
                findNavController().navigate(R.id.photoFragment, Bundle().apply {
                    putSerializable("image", imageModel)
                })
            }
        })

        launch {
            randomViewModel.flow.catch {

            }.collect {
                imageAdapter.submitData(it)
            }
        }

        binding.rv.adapter = imageAdapter

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override val coroutineContext: CoroutineContext
        get() = job
}