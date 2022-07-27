package uz.pdp.unsplashfullwallpapers.ui.photo

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import eightbitlab.com.blurview.BlurView
import eightbitlab.com.blurview.RenderScriptBlur
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import uz.pdp.unsplashfullwallpapers.R
import uz.pdp.unsplashfullwallpapers.adapters.FilterAdapter
import uz.pdp.unsplashfullwallpapers.adapters.FilterAdapter.*
import uz.pdp.unsplashfullwallpapers.databinding.FragmentFilterBinding
import uz.pdp.unsplashfullwallpapers.model.ImageModel
import uz.pdp.unsplashfullwallpapers.service.*
import kotlin.coroutines.CoroutineContext


class FilterFragment : Fragment(R.layout.fragment_filter), CoroutineScope {
    private var _binding: FragmentFilterBinding? = null
    private val binding get() = _binding!!
    lateinit var imageModel: ImageModel
    private var clickInfo = -1
    private lateinit var orgBitmap: Bitmap
    private lateinit var bitmap: Bitmap
    private val job = Job()
    private var pos = 0
    private var prog = 8

    lateinit var filterAdapter: FilterAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFilterBinding.inflate(inflater, container, false)

        fragmentInAnim(binding.navigation, binding.filter)
        clickInfo = 0

        loadBlurViews()

        imageModel = arguments?.getSerializable("image") as ImageModel

        Picasso.get().load(imageModel.urls.regular).into(binding.image)

        CoroutineScope(Dispatchers.IO).launch {
            orgBitmap = Picasso.get().load(imageModel.urls.regular).get()
            bitmap = Picasso.get().load(imageModel.urls.thumb).get()
        }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val sketchImage = SketchImage.Builder(requireContext(), orgBitmap).build()

        val imageAs = sketchImage.getImageAs(0, 80)
        binding.image.setImageBitmap(imageAs)

        binding.apply {

            binding.btnDownload.setOnClickListener { v ->
                val filterBitmap: Bitmap = loadFilterBitmap()
                val imageName: String = generateFileName()
                saveImageToStorage(context, imageName, filterBitmap)
            }


            binding.btnDone.setOnClickListener { v ->
                inAnimation(
                    binding.navigation,
                    binding.filter,
                    binding.view2Top,
                    binding.view2Bottom
                )
                clickInfo = 1
            }

            binding.btnBack2.setOnClickListener { v ->
                outAnimation(
                    binding.view2Top,
                    binding.view2Bottom,
                    binding.navigation,
                    binding.filter
                )
                clickInfo = 0
            }

            binding.btnInstall.setOnClickListener { v ->
                inAnimation(
                    binding.view2Top,
                    binding.view2Bottom,
                    binding.intallTop,
                    binding.installBottom
                )
                clickInfo = 2
            }

            binding.btnBackInstall.setOnClickListener { v ->
                outAnimation(
                    binding.intallTop,
                    binding.installBottom,
                    binding.view2Top,
                    binding.view2Bottom
                )
                clickInfo = 1
            }

            binding.btnInstallLock.setOnClickListener { v ->
                val filterBitmap: Bitmap = loadFilterBitmap()
                setToWallPaper(context, filterBitmap, InstallType.FLAG_LOCK)
            }

            binding.btnInstallHome.setOnClickListener { v ->
                val filterBitmap: Bitmap = loadFilterBitmap()
                setToWallPaper(context, filterBitmap, InstallType.FLAG_SYSTEM)
            }

            binding.btnInstallAll.setOnClickListener { v ->
                val filterBitmap: Bitmap = loadFilterBitmap()
                setToWallPaper(context, filterBitmap, InstallType.FLAG_SYSTEM_LOCK)
            }

            binding.btnExit.setOnClickListener { v ->
                fragmentOutAnim(requireView(), binding.navigation, binding.filter)
                clickInfo = -1
            }


            seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(p0: SeekBar?, process: Int, p2: Boolean) {
                    launch {
                        prog = process
                        val imageAs1 = sketchImage.getImageAs(pos, process * 10)
                        image.setImageBitmap(imageAs1)
                    }

                }

                override fun onStartTrackingTouch(p0: SeekBar?) {}
                override fun onStopTrackingTouch(p0: SeekBar?) {}
            })

            val SI = SketchImage.Builder(requireContext(), bitmap).build()

            filterAdapter = FilterAdapter(SI, requireContext(), object : OnFilterClickListener {
                override fun onFilterClick(position: Int) {
                    launch {
                        pos = position
                        val imageAs3 = sketchImage.getImageAs(pos, prog * 10)
                        binding.seekBar.progress = prog
                        binding.image.setImageBitmap(imageAs3)
                    }
                }
            })

            binding.filterRv.adapter = filterAdapter

        }
    }

    private fun loadFilterBitmap(): Bitmap {
        val drawable1 = binding.image.drawable as BitmapDrawable
        return drawable1.bitmap
    }

    private fun loadBlurViews() {
        loadBlur(binding.done)
        loadBlur(binding.exit)
        loadBlur(binding.back2)
        loadBlur(binding.backInstall)
        loadBlur(binding.download)
        loadBlur(binding.install)
        loadBlur(binding.installAll)
        loadBlur(binding.installHome)
        loadBlur(binding.installLock)
    }

    private fun loadBlur(blurView: BlurView) {
        val radius = 20f
        blurView.setupWith(binding.root)
            .setBlurAlgorithm(RenderScriptBlur(requireContext()))
            .setBlurRadius(radius)
            .setBlurAutoUpdate(true)
            .setOverlayColor(Color.parseColor("#40000000"))
            .setHasFixedTransformationMatrix(false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override val coroutineContext: CoroutineContext
        get() = job
}